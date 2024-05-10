package com.example.khoshgolpo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

public class registration extends AppCompatActivity {
    CircleImageView img;
    EditText rname, remail, rpassword, rrepassword;
    Button create;
    TextView rglogin;
    FirebaseAuth auth;
    Uri imgUri;
    String imgUristr;
    String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        img = findViewById(R.id.profilrg);
        rname = findViewById(R.id.rgName);
        remail = findViewById(R.id.rgemail);
        rpassword = findViewById(R.id.rgPassword);
        rrepassword = findViewById(R.id.rgrePassword);
        create = findViewById(R.id.createbutton);
        rglogin = findViewById(R.id.rglogin);

        rglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=rname.getText().toString();
                String email=remail.getText().toString();
                String pass=rpassword.getText().toString();
                String cpass=rrepassword.getText().toString();
                String status="Hey, I'm Using This App";
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||TextUtils.isEmpty(pass)
                ||TextUtils.isEmpty(cpass)){
                    Toast.makeText(registration.this,"Please Use Valid Information",Toast.LENGTH_SHORT).show();

                }
                else if (!email.matches(emailpattern)) {
                   remail.setError("Give a proper Email.");

                }
                else if (pass.length()<6) {
                    rpassword.setError("Give a proper password.");
                }
                else if(!pass.equals(cpass)){
                    rpassword.setError("Password Doesn't Match.");
                }
                else{
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id=task.getResult().getUser().getUid();
                                DatabaseReference reference=database.getReference().child("User").child(id);
                                StorageReference storageReference=storage.getReference().child("Upload").child(id);
                                if(imgUri!=null){
                                    storageReference.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imgUristr=uri.toString();
                                                        User user=new User(id,name,email,pass,imgUristr,status);
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Intent intent=new Intent(registration.this,MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(registration.this,"Error Creating User Id.",Toast.LENGTH_SHORT).show();
                                                                }



                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else{
                                    String status="Hey, I'm Using This App";
                                    imgUristr= "https://firebasestorage.googleapis.com/v0/b/khoshgolpo-242c7.appspot.com/o/NicePng_avatar-png_3012856.png?alt=media&token=176b75d5-6eb0-4821-bdde-85dbab5428ee";
                                    User user=new User(id,name,email,pass,imgUristr,status);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Intent intent=new Intent(registration.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(registration.this,"Error Creating User Id.",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });


                                }

                            }else{
                                Toast.makeText(registration.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });




                }



            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile Picture"),10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imgUri=data.getData();
                img.setImageURI(imgUri);
            }
        }
    }
}