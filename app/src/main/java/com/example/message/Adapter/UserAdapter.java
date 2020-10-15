package com.example.message.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.message.MessageActivity;
import com.example.message.Model.Chats;
import com.example.message.Model.User;
import com.example.message.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> {


    List<User> mUsers;
    Context context;
    boolean isChat;
    FirebaseUser firebaseUser;
    String friendid;
    String thelastMessage;

    public UserAdapter(Context context, List<User> mUsers, boolean isChat) {
        this.context = context;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layoutofusers, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        User user = mUsers.get(position);

        friendid = user.getId();

        holder.username.setText(user.getUsername());

        if (user.getImageURL().equals("default")) {

            holder.imageView.setImageResource(R.drawable.user);

        } else {

            Glide.with(context).load(user.getImageURL()).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("friendid", friendid);
                context.startActivity(intent);


            }
        });

       if (isChat) {

            if (user.getStatus().equals("online")) {

                holder.imageOn.setVisibility(View.VISIBLE);
                holder.imageOff.setVisibility(View.GONE);



            } else {

                holder.imageOn.setVisibility(View.GONE);
                holder.imageOff.setVisibility(View.VISIBLE);


            }
        } else {


            holder.imageOn.setVisibility(View.GONE);
            holder.imageOff.setVisibility(View.GONE);
        }



       // last seenmessage
        if (isChat) {

            LastSeenMessage(user.getId(), holder.last_msg);


        } else {

            holder.last_msg.setVisibility(View.GONE);

        }



    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView username, last_msg;
        CircleImageView imageView, imageOn, imageOff;

        public MyHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.username_userfrag);
            imageView = itemView.findViewById(R.id.image_user_userfrag);
            imageOn = itemView.findViewById(R.id.image_online);
            imageOff = itemView.findViewById(R.id.image_offline);
            last_msg = itemView.findViewById(R.id.lastMessage);
        }
    }


    private void LastSeenMessage(final String friendid, final TextView last_msg) {

        thelastMessage = "default";

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (firebaseUser!=null && chats!=null) {

                        if (chats.getReciever().equals(friendid) && chats.getSender().equals(firebaseUser.getUid()) ||
                                chats.getReciever().equals(firebaseUser.getUid()) && chats.getSender().equals(friendid)) {


                            thelastMessage = chats.getMessage();


                        }


                    }


                }


                switch (thelastMessage) {


                    case "default":
                        last_msg.setText("no message");
                        break;

                    default:
                        last_msg.setText(thelastMessage);
                        break;






                }

                thelastMessage = "default";

            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
