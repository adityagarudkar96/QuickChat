package com.ag.quickchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("deprecation")
public class registrationAcitvity extends AppCompatActivity {
    TextView txt_signin , btn_signup;
    CircleImageView profile_image;
    EditText reg_name , reg_email , reg_password;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_acitvity);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        txt_signin = findViewById(R.id.txt_signin);
        profile_image = findViewById(R.id.profile_image);
        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);
        btn_signup = findViewById(R.id.signup_btn);


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String password = reg_password.getText().toString();



                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(registrationAcitvity.this  , "Enter valid Information!" , Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern)){
                    reg_email.setError("Enter valid Email!");
                    Toast.makeText(registrationAcitvity.this  , "Enter valid email" ,  Toast.LENGTH_SHORT).show();
                }else if (password.length()<6){
                    Toast.makeText(registrationAcitvity.this  , "Enter 6 character password" ,  Toast.LENGTH_SHORT).show();
                }else{

                    auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                if (imageUri!=null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                       imageURI = uri.toString();
                                                       Users users = new Users(auth.getUid() , name , email , imageURI);
                                                       reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                               if (task.isSuccessful()){
                                                                   startActivity(new Intent(registrationAcitvity.this , homeAcitvity.class));
                                                               }else{
                                                                   Toast.makeText(registrationAcitvity.this  , "Something went wrong!" ,  Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       });
                                                    }

                                                });
                                            }
                                        }
                                    });
                                } else {
                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/quickchat-95744.appspot.com/o/profile_image.png?alt=media&token=f5d5b083-105c-4908-b121-44861257ed1d";
                                    Users users = new Users(auth.getUid() , name , email , imageURI);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                startActivity(new Intent(registrationAcitvity.this , homeAcitvity.class));
                                            }else{
                                                Toast.makeText(registrationAcitvity.this  , "Something went wrong!" ,  Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }


                            }
                            else{
                                Toast.makeText(registrationAcitvity.this  , "Something went Wrong!" ,  Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


            }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);

            }
        });

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registrationAcitvity.this , loginAcitvity.class));
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data != null){
                imageUri = data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }

}