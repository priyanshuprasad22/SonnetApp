package com.example.sonnetapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sonnetapp.databinding.FragmentFindUsersBinding;
import com.example.sonnetapp.models.Users;
import com.example.sonnetapp.recycleadapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindUsers extends Fragment {


    FragmentFindUsersBinding findUsersBinding;
    FirebaseAuth mAuth;
    String currentUserUid;
    private DatabaseReference usersReference;
    ArrayList<Users> usersArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        findUsersBinding=FragmentFindUsersBinding.inflate(inflater,container,false);
        mAuth=FirebaseAuth.getInstance();
        currentUserUid=mAuth.getUid();
        usersArrayList=new ArrayList<>();

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        usersReference=database.getReference("Users");

        Query usersQuery = usersReference.orderByKey();


        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    findUsersBinding.loadactivity.setVisibility(View.GONE);

                    if (!uid.equals(currentUserUid)) {
                        String name = userSnapshot.child("name").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);

                        usersArrayList.add(new Users(name,email,uid,"","Hello!"));
                        UserAdapter userAdapter=new UserAdapter(usersArrayList,findUsersBinding.getRoot().getContext());
                        findUsersBinding.recyclerview.setAdapter(userAdapter);
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireContext());
                        findUsersBinding.recyclerview.setLayoutManager(linearLayoutManager);


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });






        return findUsersBinding.getRoot();
    }

    public void getUsersDetailsExceptCurrentUser() {
        Query usersQuery = usersReference.orderByKey();


        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();

                    if (!uid.equals(currentUserUid)) {
                        String name = userSnapshot.child("name").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);

                        usersArrayList.add(new Users(name,email,uid,"","Hello!"));
                        UserAdapter userAdapter=new UserAdapter(usersArrayList,findUsersBinding.getRoot().getContext());
                        findUsersBinding.recyclerview.setAdapter(userAdapter);
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireContext());
                        findUsersBinding.recyclerview.setLayoutManager(linearLayoutManager);


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}