package com.example.user.loginfirebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email,password;
    private Button signin,signup;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        signin = (Button) findViewById(R.id.button2);
        signup = (Button) findViewById(R.id.button);
        email = (EditText) findViewById(R.id.emailEdt);
        password = (EditText) findViewById(R.id.passwordEdt);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                String getEmail = email.getText().toString();
                String getPassword = password.getText().toString();
                if(getEmail.isEmpty()||getPassword.isEmpty())
                    Toast.makeText(MainActivity.this, "Sign in Failed, please check your email and password",
                            Toast.LENGTH_SHORT).show();
                else
                    callSignin(getEmail, getPassword);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                String getEmail = email.getText().toString();
                String getPassword = password.getText().toString();
                if(getEmail.isEmpty()||getPassword.isEmpty())
                    Toast.makeText(MainActivity.this, "Sign up Failed, please check your email and password",
                            Toast.LENGTH_SHORT).show();
                else
                    callSignup(getEmail, getPassword);
            }
        });

    }
        private void callSignup(String email,String password){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Success", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Sign Up Failed, please check your email and password",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                userProfile();
                                Toast.makeText(MainActivity.this, "Account Created",
                                        Toast.LENGTH_SHORT).show();
                                Log.d("Test","Acoount Created");
                            }

                            // ...
                        }
                    });
        }

        private void userProfile(){
            FirebaseUser user = mAuth.getCurrentUser();
            if(user!=null){
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName((email.getText().toString())).build();
                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("test","user profile updates");
                        }
                    }
                });
            }
        }

        private void callSignin(String email,String password){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Test", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("Test", "signInWithEmail:failed", task.getException());
                                Toast.makeText(MainActivity.this, "Login Failed, please check your email and password",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                Intent i = new Intent(MainActivity.this, SignIn.class);
                                finish();
                                startActivity(i);
                            }
                            // ...
                        }
                    });
        }
    }

