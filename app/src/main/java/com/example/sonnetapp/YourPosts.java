package com.example.sonnetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.sonnetapp.databinding.ActivityYourPostsBinding;
import com.example.sonnetapp.models.Posts;
import com.example.sonnetapp.recycleadapter.PostsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YourPosts extends AppCompatActivity {

    ActivityYourPostsBinding yourPostsBinding;
    ArrayList<Posts> postsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yourPostsBinding=ActivityYourPostsBinding.inflate(getLayoutInflater());
        setContentView(yourPostsBinding.getRoot());
        postsArrayList=new ArrayList<>();

        PostsAdapter postsAdapter=new PostsAdapter(postsArrayList,this);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userId=firebaseUser.getUid();

        yourPostsBinding.recyclerviewPosts.setAdapter(postsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        yourPostsBinding.recyclerviewPosts.setLayoutManager(linearLayoutManager);




        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts").child(userId);
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot postsSnapshot) {
                yourPostsBinding.loadactivity.setVisibility(View.GONE);
                for (DataSnapshot postSnapshot : postsSnapshot.getChildren()) {
                    if(postSnapshot.exists())
                    {
                        Posts post = postSnapshot.getValue(Posts.class);
                        postsArrayList.add(post);
                    }
                    else {
                        yourPostsBinding.loadactivity.setVisibility(View.GONE);
                    }

                }
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }
}