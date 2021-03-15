package com.example.epics.ui.profile;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.epics.Login;
import com.example.epics.MainActivity;
import com.example.epics.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class ProfileFragment extends Fragment {

    TextView fb, name, points, badge;
    Button logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View myInflatedView = inflater.inflate(R.layout.fragment_profile, container,false);
        fb = myInflatedView.findViewById(R.id.feedback);
        fb.setPaintFlags(fb.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        logout = myInflatedView.findViewById(R.id.logout);
        name = myInflatedView.findViewById(R.id.name);
        points = myInflatedView.findViewById(R.id.points);
        badge = myInflatedView.findViewById(R.id.badge);
//        wishlist = myInflatedView.findViewById(R.id.wishlist);

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
//                        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
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
                final EditText editText = myInflatedView.findViewById(R.id.fb_edittext);
                final Button button = myInflatedView.findViewById(R.id.submit_fb);
                if (editText.getVisibility() == View.GONE) {
                    editText.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                } else {
                    editText.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                }
            }
        });

        return myInflatedView;
    }
}