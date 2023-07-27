package com.example.sonnetapp.recycleadapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sonnetapp.R;
import com.example.sonnetapp.models.Posts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    ArrayList<Posts> postlist;
    Context context;

    public PostsAdapter(ArrayList<Posts> postlist, Context context)
    {
        this.postlist=postlist;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_loayout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Posts current= postlist.get(position);

        holder.title.setText(current.getTitle());
        holder.description.setText(current.getDescription());
        holder.timestamp.setText(current.getTimestamp());

        Glide.with(context).load(current.getImage()).into(holder.postimage);
        holder.username.setText(current.getName());

        holder.shareimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageUrl = current.getImage();
                String contentDescription = "Check out this image: " + current.getTitle();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, contentDescription + "\n\n" + imageUrl);
                    context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                } else {
                    Toast.makeText(context, "Image URL is invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,timestamp,username;
        ImageView postimage,shareimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.post_title);
            description=itemView.findViewById(R.id.post_description);
            timestamp=itemView.findViewById(R.id.post_time);
            postimage=itemView.findViewById(R.id.post_image);
            username=itemView.findViewById(R.id.user_name);
            shareimage=itemView.findViewById(R.id.share);

        }
    }
//    private String getFileExtensionFromUrl(String imageUrl) {
//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            int lastDotIndex = imageUrl.lastIndexOf(".");
//            if (lastDotIndex != -1) {
//                return imageUrl.substring(lastDotIndex + 1,lastDotIndex+4).toLowerCase();
//            }
//        }
//        return null;
//    }
}
