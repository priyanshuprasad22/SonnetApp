package com.example.sonnetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sonnetapp.databinding.ActivitySignupBinding;
import com.example.sonnetapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignupBinding signupBinding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupBinding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(signupBinding.getRoot());

        mAuth=FirebaseAuth.getInstance();


        signupBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=signupBinding.name.getText().toString().trim();
                String email=signupBinding.email.getText().toString().trim();
                String password=signupBinding.password.getText().toString().trim();
                signup(name,email,password);
            }
        });
    }

    void signup(String name,String email,String password)
    {


        Log.d("name email",name+" "+email);

        mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                Log.d("Success","Successful Created User");

                                updateuser(user.getUid(),name,email);

                            } else {
                                Toast.makeText(SignUpActivity.this,"Unable to sign up",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    void updateuser(String uid,String name,String email)
    {
        Log.d("Updating User","user update");
        DatabaseReference mDbRef= FirebaseDatabase.getInstance().getReference();
        mDbRef.child("Users").child(uid).setValue(new Users(name,email,uid,"","")).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent=new Intent(SignUpActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }

}

