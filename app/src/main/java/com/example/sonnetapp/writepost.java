package com.example.sonnetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sonnetapp.databinding.ActivityWritepostBinding;
import com.example.sonnetapp.models.Posts;
import com.example.sonnetapp.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class writepost extends AppCompatActivity {

    ActivityWritepostBinding writepostBinding;
    private static final int REQUEST_IMAGE_PICK = 2;
    private Uri imageUri;
    private String Username,imageUrl,timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writepostBinding=ActivityWritepostBinding.inflate(getLayoutInflater());
        setContentView(writepostBinding.getRoot());

        writepostBinding.buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        writepostBinding.buttonSubmitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writepostBinding.upload.setVisibility(View.VISIBLE);
                findUser();
            }
        });


    }



    private void openGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickImageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
        } else {
            Toast.makeText(this, "Gallery app not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            imageUri = data.getData();

            Glide.with(this).load(imageUri).into(writepostBinding.imageViewSelectedImage);

        }
    }

    void findUser()
    {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    if (userName != null) {
                        Username=userName;
                        postupload(Username);
                    }
                }
            } else {
                Exception exception = task.getException();
            }
        });
    }

    void postupload(String Username)
    {
        String title=writepostBinding.editTextTitle.getText().toString().trim();
        String description=writepostBinding.editTextDescription.getText().toString().trim();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            timestamp = now.format(formatter);
        }
        String imageName = generateUniqueImageName();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images").child(imageName);

        UploadTask uploadTask = storageRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        imageUrl = downloadUri.toString();


                        DatabaseReference mDbRef= FirebaseDatabase.getInstance().getReference();
                        String postId = mDbRef.child("Posts").push().getKey();
                        assert postId != null;
                        mDbRef.child("Posts").child(uid).child(postId).setValue(new Posts(title,description,imageUrl,timestamp,Username,"")).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(writepost.this,"Image Upload Successful",Toast.LENGTH_LONG).show();
                                writepostBinding.upload.setVisibility(View.GONE);
                                Intent intent=new Intent(writepost.this,HomeActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                writepostBinding.upload.setVisibility(View.GONE);
                Toast.makeText(writepost.this, "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String generateUniqueImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String randomString = UUID.randomUUID().toString();
        return "image_" + timeStamp + "_" + randomString + ".jpg";
    }
}