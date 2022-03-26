package com.inamul.cracktheci;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inamul.users.User;

import java.util.regex.Matcher;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    EditText signupEmailText, signupUsernameText, signupPasswordText;
    Button btnSignup;
    TextView txtViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signupEmailText = findViewById(R.id.signupEmailText);
        signupUsernameText = findViewById(R.id.signupUsernameText);
        signupPasswordText = findViewById(R.id.signupPasswordText);
        txtViewLogin = findViewById(R.id.txtViewLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener((v) -> {
            String email = signupEmailText.getText().toString(), username = signupUsernameText.getText().toString(), password = signupPasswordText.getText().toString();
            if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signupEmailText.setError("Enter a valid e-mail");
                signupEmailText.requestFocus();
            } else if(username.isEmpty()) {
                signupUsernameText.setError("Username cannot be empty");
                signupUsernameText.requestFocus();
            } else if(username.length() < 3) {
                signupUsernameText.setError("Username length must be atleast 3");
                signupUsernameText.requestFocus();
            } if(username.length() > 2){
                Query usernameQuery = mDatabase.child("users").orderByChild("username").equalTo(username);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() > 0) {
                            signupUsernameText.setError("Username already exists");
                            signupUsernameText.requestFocus();
                        } else {
                            if(password.isEmpty() || password.length() < 5) {
                                signupPasswordText.setError("Password length must be atleast 5");
                                signupPasswordText.requestFocus();
                            } else {
                                ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                                progressDialog.setTitle("Logging In");
                                progressDialog.setMessage("This may take a few seconds");
                                progressDialog.show();
                                signupEmailText.setEnabled(false);
                                signupPasswordText.setEnabled(false);
                                signupUsernameText.setEnabled(false);

                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            User user = new User(email, username, password);
                                            mDatabase.child("users").child(mAuth.getUid()).setValue(user);
                                            Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            startActivity(new Intent(SignupActivity.this, CompaniesActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(SignupActivity.this, "Cannot create user " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            signupEmailText.setEnabled(true);
                                            signupUsernameText.setEnabled(true);
                                            signupPasswordText.setEnabled(true);
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        txtViewLogin.setOnClickListener((v) -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

    }
}