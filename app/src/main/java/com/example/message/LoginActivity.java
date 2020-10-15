package com.example.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    Button login;
    MaterialEditText et_email, et_password;
    String email, password;


    Toolbar toolbar;

    //fiREBASESHIT
    FirebaseAuth auth;

    //pd
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //firebaseauth

        auth = FirebaseAuth.getInstance();

        //pd
        pd = new ProgressDialog(this);

        // toolbar
        toolbar = findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//views
        et_email = findViewById(R.id.log_email);
        et_password = findViewById(R.id.log_password);





        login = findViewById(R.id.login_account);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if (email.isEmpty()) {

                    et_email.setError("Required");

                } else if (password.length() < 6) {
                    et_password.setError("Password Length must be more than 6");

                } else if (password.isEmpty()) {
                    et_password.setError("Required");

                } else {


                    pd.setMessage("Logging In");
                    pd.show();

                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {



                            if (task.isSuccessful()) {

                                pd.dismiss();



                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();




                            }

                        }
                    });
                }





            }
        });
    }
}