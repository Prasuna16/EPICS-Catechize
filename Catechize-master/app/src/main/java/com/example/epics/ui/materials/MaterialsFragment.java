package com.example.epics.ui.materials;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.epics.R;
import com.example.epics.ResponsePOJO;
import com.example.epics.RetrofitClient;
import com.example.epics.pdfViewScreen;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MaterialsFragment extends Fragment {

    private Button btnSelect, btnUpload, btnSearch;
    private TextView textView;
    private EditText editText, search_text;
    private LinearLayout linearLayout;

    private  int REQ_PDF = 21;
    private  String encodedPDF;
    String[] files, wishlisted;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.fragment_materials, container, false);
        textView = myInflatedView.findViewById(R.id.textView);
        btnSelect = myInflatedView.findViewById(R.id.btnSelect);
        btnUpload = myInflatedView.findViewById(R.id.btnUpload);
        editText = myInflatedView.findViewById(R.id.Category);
        linearLayout = myInflatedView.findViewById(R.id.place_materials);
        search_text = myInflatedView.findViewById(R.id.search_text);
        btnSearch = myInflatedView.findViewById(R.id.search_btn);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, REQ_PDF);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDocument();
            }
        });

        get_files("");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = String.valueOf(search_text.getText());
                get_files(search);
            }
        });

        return myInflatedView;
    }

    private void uploadDocument() {

        Call<ResponsePOJO> call = RetrofitClient.getInstance().getAPI().uploadDocument(encodedPDF, String.valueOf(editText.getText()));
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Toast.makeText(getContext(), response.body().getRemarks(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(getContext(), "Network Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_PDF && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(path);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                textView.setText("Document Selected");
                btnSelect.setText("Change Document");

                Toast.makeText(getContext(), "Document Selected", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    private void get_files(String search) {
        linearLayout.removeAllViews();
        String[] data = {"search", "user"};
        String[] values = {search, com.example.epics.PreferenceUtils.getUsername(getContext())};
        PutData putData = new PutData("http://192.168.43.67/EPICS/materials/get_files.php", "POST", data, values);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String r = putData.getResult();
                String[] dataa = r.split("BREAKKKK");
                files = dataa[0].split("NEXTTTT");
                wishlisted = dataa[1].split("NEXTTTT");
            }
        }

        for (int idx = 0; idx < files.length; idx++) {
            final String curr_file = files[idx];
            LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            params0.setMargins(20, 0, 20, 0);
            LinearLayout l = new LinearLayout(getActivity());
            l.setOrientation(LinearLayout.HORIZONTAL);
            l.setLayoutParams(params0);
            final TextView t = new TextView(getActivity());
            t.setId(idx);
            t.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
            t.setTextSize(18);
            t.setText(curr_file);
            LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            pp.setMargins(20, 0, 0, 0);
            t.setLayoutParams(pp);
            final TextView tt = new TextView(getActivity());
            tt.setId(idx);
            tt.setText("Save");
            tt.setTextSize(17);
            tt.setTextColor(R.color.colorPrimaryDark);
            LinearLayout.LayoutParams pp0 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            pp0.setMargins(60, 0, 20, 0);
            tt.setLayoutParams(pp0);
            for (int j = 0; j < wishlisted.length; j++) {
                if (wishlisted[j].equals(curr_file)) {
                    tt.setText("Saved!");
                }
            }
            l.addView(t);
            l.addView(tt);
            linearLayout.addView(l);

            View v = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2
            );
            if (idx == files.length - 1) {
                params.setMargins(20, 0, 20, 120);
            } else {
                params.setMargins(20, 0, 20, 20);
            }
            v.setLayoutParams(params);
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            linearLayout.addView(v);

            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), curr_file, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getContext(), pdfViewScreen.class);
                    i.putExtra("bookName", curr_file);
                    startActivity(i);
                }
            });

            tt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] data = {"book", "action", "user"};
                    String[] values = {curr_file, "saved", com.example.epics.PreferenceUtils.getUsername(getContext())};
                    if (String.valueOf(tt.getText()).equals("Save")) {
                        values[1] = "save";
                        tt.setText("Saved!");
                    } else {
                        tt.setText("Save");
                    }
                    PutData putData = new PutData("http://192.168.43.67/EPICS/materials/save_book.php", "POST", data, values);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String r = putData.getResult();
                            Toast.makeText(getContext(), r, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
}