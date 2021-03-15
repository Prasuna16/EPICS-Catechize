package com.example.epics.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.epics.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class HomeFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setTitle("Feed");
        View myInflatedView = inflater.inflate(R.layout.fragment_home, container,false);


        LinearLayout linearLayout = (LinearLayout) myInflatedView.findViewById(R.id.home_layout);
        final ScrollView scrollView = (ScrollView) myInflatedView.findViewById(R.id.scroll_view);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        System.out.println(width + " " + height);

        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        params0.setMargins(30, 0, 30, 0);
        final TextView textView = new TextView(getActivity());
        textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
        textView.setTextSize(18);
        textView.setLayoutParams(params0);
        String head = "What's going on?";
        textView.setText(head);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        params.setMargins(30, 5, 30, 0);
        final EditText editText = new EditText(getActivity());
        editText.setHint("Share your experiences, tips, articles, suggestions, and anything that helps others.");
        editText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
        editText.setLayoutParams(params);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        params1.setMargins(width - 250, 30, 30, 0);
        final Button button = new Button(getActivity());
        String post = "Post";
        button.setText(post);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button.setTextColor(getResources().getColor(R.color.white));
        button.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
        button.setTextSize(17);
        button.setLayoutParams(params1);

        linearLayout.addView(textView, 0);
        linearLayout.addView(editText, 1);
        linearLayout.addView(button, 2);

        int i = 0;
        final int constt = 10000;
        String ss[] = {"a", "b"};
        PutData putData = new PutData("http://192.168.43.67/EPICS/feed.php", "POST", ss, ss);
        PutData putData1 = new PutData("http://192.168.43.67/EPICS/desc_feed.php", "POST", ss, ss);
        if (putData.startPut() && putData1.startPut()) {
            if (putData.onComplete() && putData1.onComplete()) {
                String user = putData.getResult();
                String[] usernames = user.split("\\s+");
                String d = putData1.getResult();
                String[] descc = d.split("/nnextline");
                for (int j = 0; j < usernames.length && j < descc.length; j++) {
                    String s = usernames[j];
                    String desc = descc[j];
                    final TextView userName = new TextView(getActivity());
                    userName.setText(s);
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    userName.setId(i);
                    userName.setTextColor(getResources().getColor(android.R.color.black));
                    userName.setPadding(25, 20, 20, 0);
                    userName.setTextSize(23);
                    userName.setText(s);
                    userName.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    linearLayout.addView(userName);

                    final TextView description = new TextView(getActivity());
                    description.setText(s);
                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    description.setId(constt + i);
                    description.setTextColor(getResources().getColor(R.color.grey));
                    if (j != descc.length - 1) description.setPadding(25, 5, 20, 5);
                    else description.setPadding(25, 5, 20, 100);
                    description.setTextSize(18);
                    description.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    description.setText(desc);
                    linearLayout.addView(description);
                }
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data = new String[2];
                data[0] = "username";
                data[1] = "description";
                String[] values = new String[2];
                values[0] = com.example.epics.PreferenceUtils.getUsername(getContext());
                values[1] = String.valueOf(editText.getText());
                PutData putData = new PutData("http://192.168.43.67/EPICS/add_post.php", "POST", data, values);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String r =  putData.getResult();
                        Toast.makeText(getContext(), r, Toast.LENGTH_LONG).show();
                        if (r.equals("Post added successfully")) {
                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        final Button addPost = (Button) myInflatedView.findViewById(R.id.add_post);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0, 0);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        return myInflatedView;
    }
}