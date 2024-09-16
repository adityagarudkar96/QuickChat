package com.ag.quickchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class homeAcitvity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_acitvity);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser()==null){
       startActivity(new Intent(homeAcitvity.this, registrationAcitvity.class));
        }
    }
}