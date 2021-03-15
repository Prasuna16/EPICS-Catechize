package com.example.epics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class pdfViewScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view_screen);

        Intent i = getIntent();
        String book_name = i.getStringExtra("bookName");

        PDFView pdf_view = (PDFView) findViewById(R.id.myView);
        pdf_view.fromAsset(book_name).load();
    }
}