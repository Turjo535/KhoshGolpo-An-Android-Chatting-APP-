package com.example.khoshgolpo;

import static com.example.khoshgolpo.chatWindow.recieverImg;
//import static com.example.khoshgolpo.chatWindow.reciverIImg;
import static com.example.khoshgolpo.chatWindow.senderImg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdepter extends RecyclerView.Adapter{
    Context context;
    ArrayList<MsgModel>msgarrayList;
    int item_send=1,item_recieve=2;




    public MessageAdepter(Context context, ArrayList<MsgModel> msgarrayList) {
        this.context = context;
        this.msgarrayList = msgarrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==item_send){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderVierwHolder(view);
        }
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MsgModel mmessages = msgarrayList.get(position);
        if (holder.getClass()==senderVierwHolder.class){
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            viewHolder.msgtxt.setText(mmessages.getMsg());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(mmessages.getMsg());


            Picasso.get().load(recieverImg).into(viewHolder.circleImageView);


        }
    }

    @Override
    public int getItemCount() {
        //if(msgarrayList!=null)
        return msgarrayList.size();
        //else return 0;



    }

    @Override
    public int getItemViewType(int position) {
        MsgModel mmessages = msgarrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mmessages.getSenderId())) {
            return item_send;
        } else {
            return item_recieve;
        }
    }

    class  senderVierwHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);

        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }


}
