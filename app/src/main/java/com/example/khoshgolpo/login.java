package com.example.khoshgolpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    Button button,buttonsignin;
    EditText email, password;
    FirebaseAuth auth;
    String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        button=findViewById(R.id.logbutton1);
        email=findViewById(R.id.loginemail);
        password=findViewById(R.id.loginPassword);
        buttonsignin=findViewById(R.id.signinbutton1);
        buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,registration.class);
                startActivity(intent);
                finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=email.getText().toString();
                String pass=password.getText().toString();
                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(login.this,"Please Enter Your Email Account.",Toast.LENGTH_SHORT).show();
                }
               else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(login.this,"Please Enter Your Password.",Toast.LENGTH_SHORT).show();
                } else if (!mail.matches(emailpattern)) {
                   email.setError("Give a proper Email.");
                    
                } else if (password.length()<6 && password.length()>32) {
                    Toast.makeText(login.this,"Password length should be 6-32 characters.",Toast.LENGTH_SHORT).show();
                }
               else{
                   auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                try {
                                    Intent intent=new Intent(login.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                catch (Exception e){
                                    Toast.makeText(login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                       }
                   });
                }
            }
        });

    }
}