package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class login_page extends AppCompatActivity {
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        signInBtn=findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(v -> {
            startActivity(new Intent(login_page.this, MainActivity.class));
            finish();
        });

    }
}