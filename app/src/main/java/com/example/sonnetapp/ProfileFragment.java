package com.example.sonnetapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sonnetapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding profileBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileBinding= FragmentProfileBinding.inflate(inflater, container, false);

        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        String currUid=currentUser.getUid();


        profileBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileFragment.this.getContext(),YourPosts.class);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(currUid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    String name=snapshot.child("name").getValue(String.class);
                    String email=snapshot.child("emial").getValue(String.class);

                    profileBinding.txtName.setText(name);
                    profileBinding.txtEmail.setText(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return profileBinding.getRoot();
    }
}