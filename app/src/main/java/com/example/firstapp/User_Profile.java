package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Profile extends AppCompatActivity {

    private Button logout;
    private FirebaseUser user;
    private DatabaseReference ref;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        logout=(Button) findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(User_Profile.this,MainActivity.class));
            }
        });
        user=FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("Users");
        uid=user.getUid();

        final TextView greet=(TextView) findViewById(R.id.greeting);
        final TextView fnameText=(TextView) findViewById(R.id.fname);
        final TextView emailText=(TextView) findViewById(R.id.email);
        final TextView AgeText=(TextView) findViewById(R.id.Age);

        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User usrProf=snapshot.getValue(User.class);
                if(usrProf!=null)
                {
                    String fullname=usrProf.Fullname;
                    String email=usrProf.Email;
                    String age=usrProf.Age;

                    greet.setText("Welcome,"+fullname+"!");
                    fnameText.setText(fullname);
                    emailText.setText(email);
                    AgeText.setText(age);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_Profile.this,"Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}