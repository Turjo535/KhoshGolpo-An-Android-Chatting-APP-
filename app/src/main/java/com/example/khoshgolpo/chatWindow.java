package com.example.khoshgolpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;

public class chatWindow extends AppCompatActivity {
    String senderUid, receiverUid,receiverProPic,receiverName;
    CircleImageView profile;
    TextView recName;
    CardView sendText;
    EditText textMsg;
    FirebaseAuth firebaseauth;
    FirebaseDatabase database;
    public static String senderImg,recieverImg;
    String recieverRoom,senderRoom;
    RecyclerView madapter;
    ArrayList<MsgModel> messagesArrayList;
    MessageAdepter messageAdepter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        getSupportActionBar().hide();




        database = FirebaseDatabase.getInstance();
        firebaseauth = FirebaseAuth.getInstance();
        receiverName=getIntent().getStringExtra("namee");
        receiverProPic=getIntent().getStringExtra("recieverimg");
        receiverUid=getIntent().getStringExtra("idd");
        messagesArrayList=new ArrayList<>();
        sendText=findViewById(R.id.sendbtnn);
        textMsg=findViewById(R.id.textmsg);
        recName=findViewById(R.id.recivername);
        profile=findViewById(R.id.profileChat);

        madapter=findViewById(R.id.msgadpter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);


        madapter.setLayoutManager(linearLayoutManager);
        messageAdepter=new MessageAdepter(chatWindow.this,messagesArrayList);
        madapter.setAdapter(messageAdepter);
        /*madapter.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // Scroll to the bottom of the RecyclerView.
                    recyclerView.scrollToPosition(messageAdepter.getItemCount() - 1);
                }
            }
        });*/


        Picasso.get().load(receiverProPic).into(profile);
        recName.setText(""+receiverName);
        senderUid=firebaseauth.getUid();
        senderRoom = senderUid+receiverUid;
        recieverRoom = receiverUid+senderUid;

        DatabaseReference reference=database.getReference().child("User").child(firebaseauth.getUid());
        DatabaseReference chatReferce=database.getReference().child("chats").child("senderRoom").child("messages");
        Log.d("chatref", chatReferce+"");
        chatReferce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MsgModel moda = dataSnapshot.getValue(MsgModel.class);
                    messagesArrayList.add(moda);
                }

                messageAdepter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg=snapshot.child("propic").getValue().toString();
                recieverImg=receiverProPic;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=textMsg.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(chatWindow.this, "Enter Message", Toast.LENGTH_SHORT).show();
                }
                textMsg.setText("");


                Date date=new Date();
                MsgModel moderMsg=new MsgModel(message,senderUid, date.getTime());
                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child("senderRoom")
                        .child("messages")
                        .push().setValue(moderMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child("recieverRoom")
                                        .child("messages").push().setValue(moderMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });

                            }
                        });


            }
        });



    }
}