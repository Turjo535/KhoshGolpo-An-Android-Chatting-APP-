package com.example.khoshgolpo;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.viewholder> {
    MainActivity mainActivity;
    ArrayList<User> usersArraylist;
    public UserAdaptor(MainActivity mainActivity, ArrayList<User> usersArraylist) {
        this.mainActivity=mainActivity;
        this.usersArraylist=usersArraylist;
    }

    @NonNull
    @Override
    public UserAdaptor.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mainActivity).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdaptor.viewholder holder, int position) {
        User users=usersArraylist.get(position);
        holder.username.setText(users.name);
        holder.userstatus.setText(users.status);
        Picasso.get().load(users.propic).into(holder.profile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainActivity,chatWindow.class);
                intent.putExtra("namee",users.getName());
                intent.putExtra("recieverimg",users.getPropic());
                intent.putExtra("idd",users.getId());
                mainActivity.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArraylist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView username,userstatus;
        public viewholder(@NonNull View itemView) {


            super(itemView);
            profile=itemView.findViewById(R.id.profile);
            username=itemView.findViewById(R.id.Username);
            userstatus=itemView.findViewById(R.id.userStatus);
        }
    }
}
