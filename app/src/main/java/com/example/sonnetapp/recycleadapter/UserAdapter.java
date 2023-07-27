package com.example.sonnetapp.recycleadapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sonnetapp.R;
import com.example.sonnetapp.models.Followers;
import com.example.sonnetapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<Users> userlist;
    Context context;
    Boolean isFollowing;

    public UserAdapter(ArrayList<Users>userlist,Context context)
    {
        this.userlist=userlist;
        this.context=context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_layout,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users user=userlist.get(position);
        String uid=user.getUid();
        holder.name.setText(user.getName());
        holder.description.setText(user.getDescription());
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String userId=mAuth.getUid();


        DatabaseReference followersRef = FirebaseDatabase.getInstance().getReference("Followers").child(userId);
        followersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isFollowing = dataSnapshot.hasChild(uid);

                if (isFollowing) {
                    holder.follow_btn.setText("Following");
                    isFollowing=true;
                } else {
                    holder.follow_btn.setText("Follow");
                    isFollowing=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        holder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();
                String loggedInUserUid = FirebaseAuth.getInstance().getUid();

                mDbRef.child("Followers").child(loggedInUserUid).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            boolean currentFollowingStatus = dataSnapshot.child("following").getValue(Boolean.class);
                            boolean newFollowingStatus = !currentFollowingStatus;
                            mDbRef.child("Followers").child(loggedInUserUid).child(uid).child("following").setValue(newFollowingStatus)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Following status updated", Toast.LENGTH_LONG).show();
                                            holder.follow_btn.setText(newFollowingStatus ? "Following" : "Follow");
                                        }
                                    });
                        } else {
                            mDbRef.child("Followers").child(loggedInUserUid).child(uid).setValue(new Followers(loggedInUserUid, uid, true))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Successfully Followed", Toast.LENGTH_LONG).show();
                                            holder.follow_btn.setText("Following");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("boolean error",databaseError.toString());
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,description,follow_btn;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            name=itemView.findViewById(R.id.usernameTextView);
            description=itemView.findViewById(R.id.descriptionTextView);
            follow_btn=itemView.findViewById(R.id.button_follow);
        }
    }
}
