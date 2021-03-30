package com.example.epics.ui.profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.epics.Login;
import com.example.epics.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

class RandomString {
    static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}

public class ProfileFragment extends Fragment {

    TextView fb, name, points, badge, createGrp, code, myGroups, joinGroup, groupsIn, wishlist;
    Button logout, btnCreateGrp, joinGrpBtn, submit_fb;
    EditText groupName, joinGrpCode, fb_edittext;
    String randCode;
    LinearLayout displayGroups, groupsInLayout, wishlist_layout;
    String[] items;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View myInflatedView = inflater.inflate(R.layout.fragment_profile, container,false);
        fb = myInflatedView.findViewById(R.id.feedback);
        fb_edittext = myInflatedView.findViewById(R.id.fb_edittext);
        submit_fb = myInflatedView.findViewById(R.id.submit_fb);
        fb.setPaintFlags(fb.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        logout = myInflatedView.findViewById(R.id.logout);
        name = myInflatedView.findViewById(R.id.name);
        points = myInflatedView.findViewById(R.id.points);
        badge = myInflatedView.findViewById(R.id.badge);
        createGrp = myInflatedView.findViewById(R.id.create_grp);
        groupName = myInflatedView.findViewById(R.id.grp_name);
        code = myInflatedView.findViewById(R.id.grp_code);
        btnCreateGrp = myInflatedView.findViewById(R.id.create_grp_btn);
        displayGroups = myInflatedView.findViewById(R.id.display_groups);
        myGroups = myInflatedView.findViewById(R.id.my_groups);
        joinGroup = myInflatedView.findViewById(R.id.join_grp);
        joinGrpBtn = myInflatedView.findViewById(R.id.join_grp_btn);
        joinGrpCode = myInflatedView.findViewById(R.id.join_grp_code);
        groupsIn = myInflatedView.findViewById(R.id.groups_in);
        groupsInLayout = myInflatedView.findViewById(R.id.display_groups_in);
        wishlist_layout = myInflatedView.findViewById(R.id.wishlisted);
        wishlist = myInflatedView.findViewById(R.id.wishlisted_view);

        displayGroups.setVisibility(View.GONE);
        groupsInLayout.setVisibility(View.GONE);

        final String uname = com.example.epics.PreferenceUtils.getUsername(getContext());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = {"username"};

                String[] data = {uname};
                PutData putData = new PutData("http://192.168.43.67/epics/profile.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        String[] val = result.split("NEXTT");
                        name.setText(uname + " - " + val[0]);
                        points.setText("Points: " + String.valueOf(val[1]));
                        badge.setText("Badge: " + val[2]);
                    }
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                com.example.epics.PreferenceUtils.saveUsername(null, getContext());
                com.example.epics.PreferenceUtils.savePassword(null, getContext());
                Intent intent = new Intent(getContext(), Login.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fb_edittext.getVisibility() == View.GONE) {
                    fb_edittext.setVisibility(View.VISIBLE);
                    submit_fb.setVisibility(View.VISIBLE);
                } else {
                    fb_edittext.setVisibility(View.GONE);
                    submit_fb.setVisibility(View.GONE);
                }
            }
        });
        
        wishlist_layout.setVisibility(View.GONE);

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wishlist_layout.getVisibility() == View.GONE) {
                    wishlist_layout.removeAllViews();
                    wishlist_layout.setVisibility(View.VISIBLE);
                    String[] data = {"search", "user"};
                    String[] values = {"", com.example.epics.PreferenceUtils.getUsername(getContext())};
                    PutData putData = new PutData("http://192.168.43.67/EPICS/materials/get_files.php", "POST", data, values);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String r = putData.getResult();
                            String[] dataa = r.split("BREAKKKK");
                            String[] wishlisted = dataa[1].split("NEXTTTT");
//                            Toast.makeText(getContext(), String.valueOf(wishlisted.length), Toast.LENGTH_LONG).show();
                            for (String s : wishlisted) {
                                TextView t = new TextView(getContext());
                                t.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                                t.setTextSize(18);
                                t.setText(s);
                                LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                pp.setMargins(50, 0, 0, 0);
                                t.setLayoutParams(pp);
                                wishlist_layout.addView(t);
                            }
                        }
                    }
                }
                else {
                    wishlist_layout.setVisibility(View.GONE);
                }
            }
        });

        randCode = RandomString.getAlphaNumericString(10);
        createGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCreateGrp.getVisibility() == View.GONE) {
                    btnCreateGrp.setVisibility(View.VISIBLE);
                    code.setVisibility(View.VISIBLE);
                    randCode = RandomString.getAlphaNumericString(10);
                    code.setText("Code: " + randCode);
                    groupName.setVisibility(View.VISIBLE);
                } else {
                    btnCreateGrp.setVisibility(View.GONE);
                    code.setVisibility(View.GONE);
                    groupName.setVisibility(View.GONE);
                }
            }
        });

        btnCreateGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(groupName.getText()).trim().equals("")) {
                    Toast.makeText(getContext(), "Please fill out the group name", Toast.LENGTH_LONG).show();
                    return;
                }
                String[] data = {"code", "grp_name", "owner"};
                String[] values = {randCode, String.valueOf(groupName.getText()), uname};
                PutData putData = new PutData("http://192.168.43.67/epics/profile/create_grp.php", "POST", data, values);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        Toast.makeText(getContext(), putData.getResult(), Toast.LENGTH_LONG).show();
                        btnCreateGrp.setVisibility(View.GONE);
                        code.setVisibility(View.GONE);
                        groupName.setVisibility(View.GONE);
                    }
                }
            }
        });

        myGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayGroups.getVisibility() == View.GONE) {
                    displayGroups.removeAllViews();
                    LinearLayout.LayoutParams params00 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    params00.setMargins(50, 10, 40, 0);
                    final TextView textView1 = new TextView(getActivity());
                    textView1.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    textView1.setTextSize(18);
                    textView1.setText("Group name  -  Group Code  -  Action");
                    textView1.setLayoutParams(params00);
                    displayGroups.addView(textView1);

                    View v1 = new View(getContext());
                    LinearLayout.LayoutParams params11 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            4
                    );
                    params11.setMargins(50, 0, 40, 20);
                    v1.setLayoutParams(params11);
                    v1.setBackgroundColor(Color.parseColor("#B3B3B3"));

                    displayGroups.addView(v1);
                    displayGroups.setVisibility(View.VISIBLE);
                    String[] data = {"owner"};
                    String[] values = {uname};
                    PutData putData = new PutData("http://192.168.43.67/epics/profile/get_grps.php", "POST", data, values);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String r = putData.getResult();
                            if (r.equals("No groups")) {
                                displayGroups.removeAllViews();
                                LinearLayout.LayoutParams params001 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                params001.setMargins(50, 10, 40, 0);
                                final TextView textView11 = new TextView(getActivity());
                                textView11.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                                textView11.setTextSize(18);
                                textView11.setText("You didn't create any group yet!");
                                textView11.setLayoutParams(params001);
                                displayGroups.addView(textView11);
                                return;
                            }
                            String[] groups = r.split("NEXTTLINE");
                            for (String group : groups) {
                                items = group.split("NEXTT");
                                LinearLayout linearLayout = new LinearLayout(getActivity());
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                                LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                params0.setMargins(50, 0, 20, 0);
                                TextView textView = new TextView(getActivity());
                                textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                                textView.setTextSize(16);
                                textView.setText(items[1] + "  -  " + items[0] + "  -");
                                textView.setLayoutParams(params0);
                                textView.setTextIsSelectable(true);
                                linearLayout.addView(textView);

                                LinearLayout.LayoutParams params01 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                params01.setMargins(20, 0, 0, 0);
                                TextView delete_grp = new TextView(getActivity());
                                delete_grp.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                                delete_grp.setTextSize(19);
//                                delete_grp.setBackgroundColor(getResources().getColor(R.color.design_default_color_error));
                                delete_grp.setTextColor(getResources().getColor(R.color.design_default_color_error));
                                delete_grp.setText("Delete");
                                delete_grp.setLayoutParams(params01);
                                linearLayout.addView(delete_grp);

                                displayGroups.addView(linearLayout);

                                View vv = new View(getContext());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        2
                                );
                                params.setMargins(50, 0, 40, 20);
                                vv.setLayoutParams(params);
                                vv.setBackgroundColor(Color.parseColor("#B3B3B3"));
                                displayGroups.addView(vv);

                                delete_grp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String[] data = {"code"};
                                        String[] values = {items[0]};
                                        PutData putData = new PutData("http://192.168.43.67/epics/profile/delete_cmpt.php", "POST", data, values);
                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                Toast.makeText(getContext(), putData.getResult(), Toast.LENGTH_LONG).show();
                                                displayGroups.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else {
                    displayGroups.setVisibility(View.GONE);
                }
            }
        });

        groupsIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupsInLayout.getVisibility() == View.GONE) {
                    groupsInLayout.removeAllViews();
                    LinearLayout.LayoutParams params00 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    params00.setMargins(50, 10, 40, 0);
                    final TextView textView1 = new TextView(getActivity());
                    textView1.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                    textView1.setTextSize(18);
                    textView1.setText("Group name  -  Group Code  -  Action");
                    textView1.setLayoutParams(params00);
                    groupsInLayout.addView(textView1);

                    View v1 = new View(getContext());
                    LinearLayout.LayoutParams params11 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            4
                    );
                    params11.setMargins(50, 0, 40, 20);
                    v1.setLayoutParams(params11);
                    v1.setBackgroundColor(Color.parseColor("#B3B3B3"));

                    groupsInLayout.addView(v1);
                    groupsInLayout.setVisibility(View.VISIBLE);
                    String[] data = {"owner"};
                    String[] values = {uname};
                    PutData putData = new PutData("http://192.168.43.67/epics/profile/grps_in.php", "POST", data, values);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String r = putData.getResult();
                            if (r.equals("No groups")) {
                                groupsInLayout.removeAllViews();
                                LinearLayout.LayoutParams params001 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                params001.setMargins(50, 10, 40, 0);
                                final TextView textView11 = new TextView(getActivity());
                                textView11.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                                textView11.setTextSize(18);
                                textView11.setText("You are not in any group yet!");
                                textView11.setLayoutParams(params001);
                                groupsInLayout.addView(textView11);
                                return;
                            }
                            String[] groups = r.split("NEXTTLINE");
                            for (String group : groups) {
                                items = group.split("NEXTT");
                                LinearLayout linearLayout = new LinearLayout(getActivity());
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                                LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                params0.setMargins(50, 0, 20, 0);
                                TextView textView = new TextView(getActivity());
                                textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                                textView.setTextSize(16);
                                textView.setText(items[1] + "  -  " + items[0] + "  -");
                                textView.setLayoutParams(params0);
                                textView.setTextIsSelectable(true);
                                linearLayout.addView(textView);

                                LinearLayout.LayoutParams params01 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                params01.setMargins(20, 0, 0, 0);
                                TextView delete_grp = new TextView(getActivity());
                                delete_grp.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
                                delete_grp.setTextSize(19);
//                                delete_grp.setBackgroundColor(getResources().getColor(R.color.design_default_color_error));
                                delete_grp.setTextColor(getResources().getColor(R.color.design_default_color_error));
                                delete_grp.setText("Exit");
                                delete_grp.setLayoutParams(params01);
                                linearLayout.addView(delete_grp);

                                groupsInLayout.addView(linearLayout);

                                View vv = new View(getContext());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        2
                                );
                                params.setMargins(50, 0, 40, 20);
                                vv.setLayoutParams(params);
                                vv.setBackgroundColor(Color.parseColor("#B3B3B3"));
                                groupsInLayout.addView(vv);

                                delete_grp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String[] data = {"code", "user"};
                                        String[] values = {items[0], uname};
                                        PutData putData = new PutData("http://192.168.43.67/epics/profile/delete_grp.php", "POST", data, values);
                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                Toast.makeText(getContext(), putData.getResult(), Toast.LENGTH_LONG).show();
                                                groupsInLayout.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else {
                    groupsInLayout.setVisibility(View.GONE);
                }
            }
        });

        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joinGrpCode.getVisibility() == View.GONE) {
                    joinGrpCode.setVisibility(View.VISIBLE);
                    joinGrpBtn.setVisibility(View.VISIBLE);
                } else {
                    joinGrpCode.setVisibility(View.GONE);
                    joinGrpBtn.setVisibility(View.GONE);
                }
            }
        });

        joinGrpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data = {"code", "user"};
                String[] values = {String.valueOf(joinGrpCode.getText()), uname};
                PutData putData = new PutData("http://192.168.43.67/epics/profile/join_grp.php", "POST", data, values);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        Toast.makeText(getContext(), putData.getResult(), Toast.LENGTH_LONG).show();
                        joinGrpCode.setVisibility(View.GONE);
                        joinGrpBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

        return myInflatedView;
    }
}