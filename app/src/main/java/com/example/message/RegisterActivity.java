package com.example.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    MaterialEditText et_email, et_password, et_username;
    Button register;
    String email, username, password;


    //Firebase

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //firebaseShit

        auth = FirebaseAuth.getInstance();

        //toolbar shit

        toolbar = findViewById(R.id.toolbarregis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //casting the views

        et_email = findViewById(R.id.reg_email);
        et_password = findViewById(R.id.reg_password);
        et_username = findViewById(R.id.reg_username);
        register = findViewById(R.id.register_Account_btn);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 email = et_email.getText().toString();
                 password = et_password.getText().toString();
                 username = et_username.getText().toString();


                 if (email.isEmpty()) {

                     et_email.setError("Required");

                 } else if (password.length() < 6) {
                     et_password.setError("Password Length must be more than 6");

                 } else if (password.isEmpty()) {
                     et_password.setError("Required");

                 } else  if (username.isEmpty()) {
                     et_username.setError("Required");

                 } else {

                     registerUser(username, email, password);
                     Toast.makeText(RegisterActivity.this, "Reigstered Success", Toast.LENGTH_SHORT).show();
                     finish();
                 }

            }
        });








    }

    private void registerUser(final String username, final String email, String password) {


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



                if (task.isSuccessful()) {

                    FirebaseUser user = auth.getCurrentUser();

                    if (user!=null) {

                        String myid = user.getUid();

                        // now here we are creating a node and a child
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(myid);

                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("username", username);
                        hashMap.put("email", email);
                        hashMap.put("imageURL", "default");
                        hashMap.put("id", myid);
                        hashMap.put("status", "offline");


                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));



                                }
                            }
                        });

                    }



                }


            }
        });



    }
}