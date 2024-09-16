package com.ag.quickchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginAcitvity extends AppCompatActivity {
    TextView txt_signup;
    EditText login_email, login_password;
    TextView signIn_btn;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitvity);
        txt_signup = findViewById(R.id.txt_signup);
        signIn_btn = findViewById(R.id.signin_btn);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        auth = FirebaseAuth.getInstance();


        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_email.getText().toString();
                String password = login_password.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(loginAcitvity.this, "Enter valid Information!!", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    login_email.setError("Invalid Email!!");
                    Toast.makeText(loginAcitvity.this, "Please enter valid Email!", Toast.LENGTH_SHORT).show();

                } else if (password.length() < 6) {
                    login_password.setError("Invalid Password!!");
                    Toast.makeText(loginAcitvity.this, "Please enter valid password!", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(loginAcitvity.this, homeAcitvity.class));
                            } else {
                                Toast.makeText(loginAcitvity.this, "Error in login", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
            });






        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginAcitvity.this , registrationAcitvity.class));
            }
        });

    }
}