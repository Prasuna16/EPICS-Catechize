package com.example.epics.ui.discuss;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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

import static android.view.View.GONE;

public class DiscussFragment extends Fragment {

    private com.example.epics.ui.profile.ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setTitle("Feed");
        View myInflatedView = inflater.inflate(R.layout.fragment_discuss, container, false);

        LinearLayout linearLayout = (LinearLayout) myInflatedView.findViewById(R.id.discuss_layout);
        final ScrollView scrollView = (ScrollView) myInflatedView.findViewById(R.id.scroll_view_discuss);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        System.out.println(width + " " + height);

        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        params0.setMargins(30, 0, 30, 0);
        final TextView textView = new TextView(getActivity());
        textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
        textView.setTextSize(18);
        textView.setLayoutParams(params0);
        String head = "Having a question disturbing your mind?";
        textView.setText(head);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(30, 5, 30, 0);
        final EditText editText = new EditText(getActivity());
        editText.setHint("Don't worry, your friends are here to help!! Ask your question here and get it solved.");
        editText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
        editText.setLayoutParams(params);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        params1.setMargins(width - 250, 10, 30, 0);
        final Button button = new Button(getActivity());
        String post = "Ask!";
        button.setText(post);
        button.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
        button.setTextSize(18);
        button.setLayoutParams(params1);

        linearLayout.addView(textView, 0);
        linearLayout.addView(editText, 1);
        linearLayout.addView(button, 2);

        int i = 0;
        final int constt = 10000;
        String[] ss = {"a"};
        PutData putData1 = new PutData("http://192.168.43.67/EPICS/discuss/get_ques.php", "POST", ss, ss);
        if (putData1.startPut()) {
            if (putData1.onComplete()) {
                String r = putData1.getResult();
                String[] myArray = r.split("NEXTTTT");
                for (int it = 0; it < myArray.length; it++) {
                    final String s = myArray[it].trim();
                    if (s.equals("")) continue;
                    final TextView userName = new TextView(getActivity());
                    userName.setText(s);
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    userName.setId(i);
                    userName.setTextColor(getResources().getColor(android.R.color.black));
                    userName.setPadding(25, 20, 20, 0);
                    userName.setTextSize(20);
                    userName.setText(s);
                    userName.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    linearLayout.addView(userName);

                    LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    params4.setMargins(60, 10, 20, 0);
                    final TextView answer = new TextView(getActivity());
                    answer.setText("Answer");
                    answer.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    answer.setPaintFlags(answer.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    answer.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    answer.setTextSize(19);
                    answer.setLayoutParams(params4);
                    answer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.answer, 0, 0, 0);

                    LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    params5.setMargins(30, 10, 00, 0);
                    final TextView getAnswers = new TextView(getActivity());
                    getAnswers.setText("Previous Answers");
                    getAnswers.setTextColor(getResources().getColor(R.color.green));
                    getAnswers.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    getAnswers.setTextSize(19);
                    getAnswers.setPaintFlags(getAnswers.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    getAnswers.setLayoutParams(params5);
                    getAnswers.setCompoundDrawablesWithIntrinsicBounds(R.drawable.prev_answers, 0, 0, 0);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    final LinearLayout linearLayout1 = new LinearLayout(getActivity());
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout1.addView(getAnswers, 0);
                    linearLayout1.addView(answer, 1);

                    LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    params6.setMargins(30, 5, 30, 0);
                    final EditText answer_editText = new EditText(getActivity());
                    answer_editText.setHint("Your Answer");
                    answer_editText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    answer_editText.setLayoutParams(params6);

                    LinearLayout.LayoutParams params7 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    params7.setMargins(width - 370, 30, 30, 0);
                    final Button submit_answer = new Button(getActivity());
                    submit_answer.setText("Submit Answer");
                    submit_answer.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    submit_answer.setTextSize(15);
                    submit_answer.setLayoutParams(params7);

                    final TextView description = new TextView(getActivity());
                    description.setText(s);
                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    description.setId(constt + i);
                    description.setTextColor(getResources().getColor(R.color.grey));
                    description.setPadding(35, 5, 20, 5);
                    description.setTextSize(18);
                    description.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    description.setText("hello just checking lorem ipsum hello just checking lorem ipsum hello just checking lorem ipsum hello just checking lorem ipsum hello just checking lorem ipsum");
                    description.setVisibility(View.GONE);

                    linearLayout.addView(linearLayout1);
                    linearLayout.addView(answer_editText);
                    answer_editText.setVisibility(GONE);
                    linearLayout.addView(submit_answer);
                    submit_answer.setVisibility(GONE);
                    linearLayout.addView(description);
                    description.setVisibility(GONE);

                    answer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (answer_editText.getVisibility() == GONE) {
                                answer_editText.setVisibility(View.VISIBLE);
                                submit_answer.setVisibility(View.VISIBLE);
                            } else {
                                answer_editText.setVisibility(GONE);
                                submit_answer.setVisibility(GONE);
                            }
                        }
                    });

                    getAnswers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (description.getVisibility() == GONE) {
                                String[] data = {"question"};
                                String[] values = {s};
                                PutData putData = new PutData("http://192.168.43.67/EPICS/discuss/get_ans.php", "POST", data, values);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String res = "";
                                        String r =  putData.getResult();
                                        String[] ans = r.split("NEXTTTT");
                                        for (int jt = 0; jt < ans.length; jt++) {
                                            String jtt = ans[jt].trim();
                                            if (jtt.equals("")) continue;
                                            if (jt == ans.length - 1) res += "Answer" + (jt + 1) + ":\n" + jtt;
                                            else res += "Answer" + (jt + 1) + ":\n" + jtt + "\n\n";
                                        }
                                        description.setText(res.equals("") ? "No answers yet!" : res);
                                        description.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                description.setVisibility(GONE);
                            }
                        }
                    });

                    submit_answer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] data = new String[3];
                            data[0] = "username";
                            data[1] = "question";
                            data[2] = "answer";
                            String[] values = new String[3];
                            values[0] = com.example.epics.PreferenceUtils.getUsername(getContext());
                            values[1] = s;
                            values[2] = String.valueOf(answer_editText.getText());

                            PutData putData = new PutData("http://192.168.43.67/EPICS/discuss/add_ans.php", "POST", data, values);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String r =  putData.getResult();
                                    if (r.equals("Success")) {
                                        Toast.makeText(getContext(), "Answer added!", Toast.LENGTH_SHORT).show();
                                        answer_editText.setText("");
                                        answer_editText.setVisibility(GONE);
                                        submit_answer.setVisibility(GONE);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }

        final TextView forSpace = new TextView(getActivity());
        forSpace.setId(constt + i);
        forSpace.setPadding(0, 0, 0, 130);

        linearLayout.addView(forSpace);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data = new String[2];
                data[0] = "username";
                data[1] = "question";
                String[] values = new String[2];
                values[0] = com.example.epics.PreferenceUtils.getUsername(getContext());
                values[1] = String.valueOf(editText.getText());
                PutData putData = new PutData("http://192.168.43.67/EPICS/discuss/add_ques.php", "POST", data, values);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String r =  putData.getResult();
                        if (r.equals("Success")) {
                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        final Button ask_ques = (Button) myInflatedView.findViewById(R.id.ask_ques);

        ask_ques.setOnClickListener(new View.OnClickListener() {
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