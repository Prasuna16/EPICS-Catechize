package com.example.epics.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.epics.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
//    private static String[] paths = {"Post Publicly", "Post Anonymously", "Post to a group"};
    ArrayList<String> paths = new ArrayList<>();
    int selected = 0;
    HashMap<String, String> mapper = new HashMap<>();
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

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        params1.setMargins(0, 0, 20, 0);
        final Button button = new Button(getActivity());
        String post = "Post";
        button.setText(post);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button.setTextColor(getResources().getColor(R.color.white));
        button.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
        button.setTextSize(17);
        button.setLayoutParams(params1);

        String[] data = {"owner"};
        paths.add("Post publicly");
        paths.add("Post anonymously");
        String[] values = {com.example.epics.PreferenceUtils.getUsername(getContext())};
        PutData pd = new PutData("http://192.168.43.67/epics/profile/grps_in.php", "POST", data, values);
        if (pd.startPut()) {
            if (pd.onComplete()) {
                String r = pd.getResult();
                if (!r.equals("No groups")) {
                    String[] groups = r.split("NEXTTLINE");
                    for (String group : groups) {
                        String[] items = group.split("NEXTT");
                        mapper.put(items[1], items[0]);
                        paths.add("Post to " + items[1]);
                    }
                }
            }
        }

        linearLayout.addView(textView, 0);
        linearLayout.addView(editText, 1);
        LinearLayout postType = new LinearLayout(getActivity());
        postType.setOrientation(LinearLayout.HORIZONTAL);
        Spinner spinner = new Spinner(getActivity());
        final LinearLayout.LayoutParams pa1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        pa1.setMargins(30, 0, 10, 0);
        spinner.setLayoutParams(pa1);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
//                Toast.makeText(getContext(), selected + " " + paths.get(selected), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected = 0;
            }
        });
        postType.addView(spinner);
        postType.addView(button);
        linearLayout.addView(postType, 2);

        int i = 0;
        final int constt = 10000;
        String[] dataa = {"user"};
        String[] val = {com.example.epics.PreferenceUtils.getUsername(getContext())};
//        PutData putData = new PutData("http://192.168.43.67/EPICS/feed.php", "POST", ss, ss);
        PutData putData1 = new PutData("http://192.168.43.67/EPICS/desc_feed.php", "POST", dataa, val);
        if (putData1.startPut()) {
            if (putData1.onComplete()) {
                String d = putData1.getResult();
//                Toast.makeText(getContext(), d, Toast.LENGTH_LONG).show();
                if (d.trim().equals("")) {
                    final TextView description = new TextView(getActivity());
                    description.setText("No posts yet!");
                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    description.setId(constt + i);
                    description.setTextColor(getResources().getColor(R.color.grey));
                    description.setPadding(35, 5, 20, 5);
                    description.setTextSize(18);
                    description.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    linearLayout.addView(description);
                } else {
                    String[] descc = d.split("NEXTTUSER");
                    for (int j = 0; j < descc.length; j++) {
                        String[] item = descc[j].split("NEXTT");
                        String s = item[0];
                        String desc = item[1];
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
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == 0) {
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
                } else {
                    if (selected == 1) {
                        String[] data = new String[2];
                        data[0] = "username";
                        data[1] = "description";
                        String[] values = new String[2];
                        values[0] = "Anonymous";
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
                    } else {
                        String grpName = paths.get(selected);
                        grpName = grpName.substring(8);
                        String code = mapper.get(grpName);
                        String[] data = {"username", "description", "code", "grpName"};
                        Toast.makeText(getContext(), grpName, Toast.LENGTH_LONG).show();
                        String[] values = {com.example.epics.PreferenceUtils.getUsername(getContext()), String.valueOf(editText.getText()), code, grpName};
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