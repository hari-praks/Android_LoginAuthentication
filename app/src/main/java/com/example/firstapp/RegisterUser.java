package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView title,registerUser;
    private EditText editName,editAge,editMail,editPwd;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();
        title = (TextView) findViewById(R.id.title);
        title.setOnClickListener(this);
        registerUser=(Button) findViewById(R.id.Register);
        registerUser.setOnClickListener(this);
        editName=(EditText) findViewById(R.id.Name);
        editAge=(EditText) findViewById(R.id.Age);
        editMail=(EditText) findViewById(R.id.Email);
        editPwd=(EditText) findViewById(R.id.pwd);

        progressBar=(ProgressBar) findViewById(R.id.PBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.Register:
                registerUser();
                break;

        }
    }
    private void registerUser() {
        String mail = editMail.getText().toString().trim();
        String pass = editPwd.getText().toString().trim();
        String fname = editName.getText().toString().trim();
        String userage = editAge.getText().toString().trim();


        if (fname.isEmpty()) {
            editName.setError("Full Name is required");
            editName.requestFocus();
            return;
        }
        if (userage.isEmpty()) {
            editAge.setError("Age is required");
            editAge.requestFocus();
            return;
        }
        if (mail.isEmpty()) {
            editMail.setError("Email is required");
            editMail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            editMail.setError("Please enter valid email");
            editMail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            editPwd.setError("Password is required");
            editPwd.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            editPwd.setError("Length must be greater than 6");
            editPwd.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fname, userage, mail);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(RegisterUser.this, "Failed to register", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(RegisterUser.this, "Failed to register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });





    }
}