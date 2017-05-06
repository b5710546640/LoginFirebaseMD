package com.example.user.loginfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView signinEmail;
    private Button signout,newpost,viewpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        signinEmail = (TextView)findViewById(R.id.signupEmailTxt);
        signout = (Button)findViewById(R.id.signoutBtn);
        newpost = (Button)findViewById(R.id.newPostBtn);
        viewpost = (Button)findViewById(R.id.viewPostBtn);

        signinEmail.setText("Hello, "+ mAuth.getCurrentUser().getEmail());

        newpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewPost.class));
            }
        });

        viewpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewAllPost.class));
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = mAuth.getCurrentUser().getEmail().toString();
                callSignout();
            }
        });
    }

    private void callSignout(){
        mAuth.signOut();
        Intent i = new Intent(SignIn.this, MainActivity.class);
        finish();
        startActivity(i);
    }
}
