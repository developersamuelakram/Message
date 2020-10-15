package com.example.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.message.Adapter.MessageAdapter;
import com.example.message.Model.Chats;
import com.example.message.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    EditText et_message;
    Button send;
    FirebaseUser firebaseUser;

    String myid, friendid, message;
    List<Chats> chatslist;
    MessageAdapter messageAdapter;
    String imageURL;


    Toolbar toolbar;
    CircleImageView imageView;
    TextView textusername;

    ValueEventListener seenList;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.recyclerview_messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar); // this is ensuring that this toolbar is our action bar
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        //views castings
        imageView = findViewById(R.id.profile_image_toolbar_message);
        textusername = findViewById(R.id.username_ontoolbar_message);





        friendid = getIntent().getStringExtra("friendid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myid = firebaseUser.getUid();


         reference = FirebaseDatabase.getInstance().getReference("Users").child(friendid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if (user.getId().equals(friendid)) {

                    textusername.setText(user.getUsername());
                }

                if (user.getImageURL().equals("default")) {

                    imageView.setImageResource(R.drawable.user);

                } else {

                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(imageView);
                }

                readMessage(myid, friendid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seenMessage(friendid);





        et_message = findViewById(R.id.message_Et_text);
        send = findViewById(R.id.send_messsage_btn);


        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() > 0) {

                    send.setEnabled(true);


                } else {
                    send.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String text = et_message.getText().toString();

                if (!text.startsWith(" ")) {

                    et_message.getText().insert(0, " ");

                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = et_message.getText().toString();

                if (TextUtils.isEmpty(message)) {
                    et_message.setError("Type Something Bitch");

                } else {


                    sendMessage(myid, friendid, message);
                }

                et_message.setText(" ");



            }
        });






    }


    private void seenMessage(final String friendid) {

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenList = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Chats chati = ds.getValue(Chats.class);

                    if (chati.getReciever().equals(myid) &&  chati.getSender().equals(friendid)) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        ds.getRef().updateChildren(hashMap);


                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void sendMessage(final String myid, final String friendid, final String message) {


        // the reason we have created a child and not a reference because it is new child.. and push
        //will create each message with unique id.
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("sender", myid);
                hashMap.put("reciever", friendid);
                hashMap.put("message", message);
                hashMap.put("isseen", false);


        reference.child("Chats").push().setValue(hashMap);



                // for making a a chat list
        // to display in chat frag

        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(myid).child(friendid);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if  (!snapshot.exists()) {

                    reference1.child("id").setValue(friendid);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void readMessage(final String myid, final String friendid, final String imageURL) {

        chatslist = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatslist.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);

                    if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                    chats.getReciever().equals(myid) && chats.getSender().equals(friendid)) {

                        chatslist.add(chats);

                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, chatslist, imageURL);
                    recyclerView.setAdapter(messageAdapter);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void status(String status) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);



    }


    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        reference.removeEventListener(seenList);

        status("offline");

    }
}