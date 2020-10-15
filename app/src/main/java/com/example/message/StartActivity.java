package com.example.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    Button Login, Register;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        auth = FirebaseAuth.getInstance();




        Login = findViewById(R.id.loginBtn);
        Register = findViewById(R.id.registerbtn);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StartActivity.this, RegisterActivity.class));

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser user = auth.getCurrentUser();

        if (user!=null) {

            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();

        }
    }
}