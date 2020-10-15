package com.example.message.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.message.Adapter.UserAdapter;
import com.example.message.Model.User;
import com.example.message.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {


    RecyclerView recyclerView;
    List<User> userList;
    UserAdapter mAdapter;


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_users);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();


        readUsers();




        return view;

    }

    private void readUsers() {

        userList = new ArrayList<>();


        final FirebaseUser  firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // if I find it by the user by the child then it will only display
        // the child of that specific userid.but below we are saying that if the user id is of user logged in dont
        //show anything that is why we will refer the whole database.
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    User user = ds.getValue(User.class);

                    if (!user.getId().equals(firebaseUser.getUid())) {

                        userList.add(user);

                    }




                    mAdapter = new UserAdapter(getContext(), userList, false);
                    recyclerView.setAdapter(mAdapter);





                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}