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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextMail,editTextPwd;
    private Button Signin;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = (TextView) findViewById(R.id.reg);
        register.setOnClickListener(this);

        Signin=(Button) findViewById(R.id.Login);
        Signin.setOnClickListener(this);

        editTextMail=(EditText) findViewById(R.id.Email);
        editTextPwd=(EditText) findViewById(R.id.pwd);
        progressBar=(ProgressBar) findViewById(R.id.PBar);
        mAuth=FirebaseAuth.getInstance();
    }
        @Override
        public void onClick (View v)
        {
            switch (v.getId()) {
                case R.id.reg:
                    startActivity(new Intent(this, RegisterUser.class));
                    break;
                case R.id.Login:
                    userLogin();
                    break;
            }

        }
        private void userLogin()
        {
            String email=editTextMail.getText().toString().trim();
            String pwd=editTextPwd.getText().toString().trim();
            if(email.isEmpty()){
                editTextMail.setError("Email is required");
                editTextMail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                editTextMail.setError("Enter valid email");
                editTextMail.requestFocus();
                return;
            }
            if(pwd.isEmpty())
            {
                editTextPwd.setError("Password is required");
                editTextPwd.requestFocus();
                return;
            }
            if (pwd.length() < 6) {
                editTextPwd.setError("Length must be greater than 6");
                editTextPwd.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified())
                        {
                            startActivity(new Intent(MainActivity.this,User_Profile.class));
                        }
                        else
                        {
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this,"Check your email to Verify your account ",Toast.LENGTH_LONG).show();
                        }


                    }else{
                        Toast.makeText(MainActivity.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    }
