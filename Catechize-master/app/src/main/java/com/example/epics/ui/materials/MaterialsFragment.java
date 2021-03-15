package com.example.epics.ui.materials;

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

    private MaterialsViewModel materialsViewModel;

    private Button btnSelect, btnUpload, btnDisplay;
    private TextView textView;
    private EditText editText;

    int REQ_PDF = 21, idx = 0;
    private  String encodedPDF;
    String[] files = {"null"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_materials, container, false);
        textView = myInflatedView.findViewById(R.id.textView);
        btnSelect = myInflatedView.findViewById(R.id.btnSelect);
        btnUpload = myInflatedView.findViewById(R.id.btnUpload);
        editText = myInflatedView.findViewById(R.id.Category);
        LinearLayout linearLayout = myInflatedView.findViewById(R.id.materials_layout);


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

        String[] data = {"sample"};
        String[] values = {"sample"};
        PutData putData = new PutData("http://192.168.43.67/EPICS/materials/get_files.php", "POST", data, values);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String res = "";
                String r = putData.getResult();
                files = r.split("NEXTTTT");
            }
        }

        for (idx = 0; idx < files.length; idx++) {
            final String curr_file = files[idx];
            LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            params0.setMargins(20, 0, 20, 0);
            final TextView textView = new TextView(getActivity());
            textView.setId(idx);
            textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.helvetica));
            textView.setTextSize(18);
            textView.setText(curr_file);
            textView.setLayoutParams(params0);
            linearLayout.addView(textView);

            View v = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2
            );
            params.setMargins(20, 0, 20, 20);
            v.setLayoutParams(params);
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            linearLayout.addView(v);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), curr_file, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getContext(), pdfViewScreen.class);
                    i.putExtra("bookName", curr_file);
                    startActivity(i);
                }
            });
        }

        return myInflatedView;
    }
    public void uploadDocument() {

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
//        Toast.makeText(getContext(), requestCode + " " + resultCode + " " + data, Toast.LENGTH_LONG).show();
        if(requestCode == REQ_PDF && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            Toast.makeText(getContext(), (CharSequence) path, Toast.LENGTH_LONG).show();
//            try {
//                InputStream inputStream = getActivity().getContentResolver().openInputStream(path);
//                byte[] pdfInBytes = new byte[inputStream.available()];
//                inputStream.read(pdfInBytes);
//                encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
//
//                textView.setText("Document Selected");
//                btnSelect.setText("Change Document");
//
//                Toast.makeText(getContext(), "Document selected", Toast.LENGTH_SHORT).show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}