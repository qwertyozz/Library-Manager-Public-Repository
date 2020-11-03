package com.example.customauthenticationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    //Views
    TextInputLayout eEmail, ePassword;
    Button bLogin, bCreate;
    TextView forgotAccount;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    // User input
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //views instantiate
        eEmail = findViewById(R.id.textInputLayoutEmail);
        ePassword = findViewById(R.id.textInputLayoutPassword);
        bLogin = findViewById(R.id.button_login);
        bCreate = findViewById(R.id.button_createAccount);
        forgotAccount = findViewById(R.id.textView_forgotAccount);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

        forgotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotAccount();
            }
        });

        if (savedInstanceState != null) {

            email = savedInstanceState.getString("Email");
            eEmail.getEditText().setText(String.valueOf(email));

            password = savedInstanceState.getString("Password");
            ePassword.getEditText().setText(String.valueOf(password));
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("Email", email);
        outState.putString("Password", password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.getInstance().signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Login To Continue", Toast.LENGTH_SHORT).show();
        }
    }


    public void Login() {
        email = eEmail.getEditText().getText().toString().trim();
        password = ePassword.getEditText().getText().toString().trim();

        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Email and Password is empty", Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(this, "Please provide an email", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(this, "Please provide a password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            mUser = mAuth.getCurrentUser();
                            if(!mUser.isEmailVerified()){
                                Toast.makeText(MainActivity.this, "Please verify your email to continue", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Logged in as " + mUser.getDisplayName(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                        }

                }
            });
        }

    }

    public void SignUp() {
        Intent intent = new Intent(MainActivity.this, CreateAccount.class);
        startActivity(intent);
    }

    public void forgotAccount(){
        Intent passwordResetIntent = new Intent(MainActivity.this,PasswordReset.class);
        startActivity(passwordResetIntent);
    }



}