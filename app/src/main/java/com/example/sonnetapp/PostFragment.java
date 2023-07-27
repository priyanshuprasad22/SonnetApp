package com.example.sonnetapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sonnetapp.databinding.FragmentPostBinding;
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


public class PostFragment extends Fragment {

    FragmentPostBinding postBinding;
    ArrayList<Posts> postsArrayList;
    private DatabaseReference followersRef;
    private String currentUserUid;

    private PostsAdapter postsAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postBinding=FragmentPostBinding.inflate(inflater, container, false);

        postsArrayList = new ArrayList<>();
        postsAdapter = new PostsAdapter(postsArrayList, requireContext());
        postBinding.postRecyclerView.setAdapter(postsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        postBinding.postRecyclerView.setLayoutManager(linearLayoutManager);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserUid = currentUser.getUid();

            followersRef = FirebaseDatabase.getInstance().getReference("Followers").child(currentUserUid);
            followersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        fetchFollowersPosts();
                    } else {
                        postBinding.notFollowedTxt.setVisibility(View.VISIBLE);
                        postBinding.loadactivity.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        postBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PostFragment.this.getContext(),writepost.class);
                startActivity(intent);
            }
        });


        return postBinding.getRoot();
    }

    private void fetchFollowersPosts() {
        followersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postBinding.loadactivity.setVisibility(View.GONE);

                for (DataSnapshot followerSnapshot : dataSnapshot.getChildren()) {
                    boolean isFollowing = followerSnapshot.child("following").getValue(Boolean.class);
                    if (isFollowing) {
                        String followerUid = followerSnapshot.child("followerId").getValue(String.class);

                        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts").child(followerUid);
                        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot postsSnapshot) {
                                for (DataSnapshot postSnapshot : postsSnapshot.getChildren()) {
                                    Posts post = postSnapshot.getValue(Posts.class);
                                    postsArrayList.add(post);
                                }
                                postsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                postBinding.loadactivity.setVisibility(View.GONE);

            }
        });
    }
}