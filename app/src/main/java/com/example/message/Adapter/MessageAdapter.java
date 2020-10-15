package com.example.message.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.message.Model.Chats;
import com.example.message.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Myholder> {



    public static final int MESSAGE_LEFT = 0; // left for friend
    public static final int MESSAGE_RIGHT = 1; // right for myself


    Context context;
    List<Chats> chatsList;
    String imageURL;

    public MessageAdapter(Context context, List<Chats> chatsList, String imageURL) {
        this.context = context;
        this.chatsList = chatsList;
        this.imageURL = imageURL;
    }



    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if(viewType == MESSAGE_RIGHT) {

            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent ,false);
            return new Myholder(view);
        } else {


            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent ,false);
            return new Myholder(view);

        }




    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        Chats chat = chatsList.get(position);

        holder.message.setText(chat.getMessage());

        if (imageURL.equals("default")) {

            holder.imageView.setImageResource(R.drawable.user);
        } else {
            Glide.with(context).load(imageURL).into(holder.imageView);
        }


        if (position == chatsList.size() - 1) {

            if (chat.isIsseen()) {

                holder.seen.setText("seen");

            } else {
                holder.seen.setText("delivered");
            }


        } else {
            holder.seen.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{


        TextView message, seen;
        CircleImageView imageView;


        public Myholder(@NonNull View itemView) {
            super(itemView);


            message = itemView.findViewById(R.id.show_message);
            imageView = itemView.findViewById(R.id.chat_image);
            seen = itemView.findViewById(R.id.text_Seen);


        }
    }


    @Override
    public int getItemViewType(int position) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // if i am the user display right layout if i M NOT DISPLAY LEFT

        assert user != null;
        if(chatsList.get(position).getSender().equals(user.getUid())) {

            return MESSAGE_RIGHT;
        } else {

            return MESSAGE_LEFT;

        }

    }
}
