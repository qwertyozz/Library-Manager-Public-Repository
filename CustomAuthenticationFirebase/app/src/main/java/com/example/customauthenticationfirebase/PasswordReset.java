package com.example.customauthenticationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordReset extends AppCompatActivity {

    //Views
    TextInputLayout passwordResetEmail;
    Button passwordResetButton;

    //Firebase
    FirebaseAuth mAuth;

    // Email variable
    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        passwordResetEmail = findViewById(R.id.textView_PasswordReset);
        passwordResetButton = findViewById(R.id.buttonResetPassword);

        passwordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordReset();
            }
        });

    }

    public void PasswordReset(){

        mAuth = FirebaseAuth.getInstance();
        emailAddress = passwordResetEmail.getEditText().getText().toString().trim();

        if(!emailValidation()){
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(PasswordReset.this, "Email sent for password reset", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PasswordReset.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public boolean emailValidation(){
        if(emailAddress.isEmpty()){
            passwordResetEmail.setError("Enter an email address");
            return false;
        }else{
            passwordResetEmail.setError(null);
            return true;
        }
    }
}