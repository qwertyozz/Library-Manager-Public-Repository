package com.example.customauthenticationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAccount extends AppCompatActivity {

    //Views
    TextInputLayout eUsername, eEmail, ePassword, textInputFullName, textInputContact;
    Button bSignUp;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Views instantiate
        eUsername = findViewById(R.id.editText_username);
        eEmail = findViewById(R.id.editText_email);
        ePassword = findViewById(R.id.editText_password);

        textInputContact = findViewById(R.id.textInputContactNumber);
        textInputFullName = findViewById(R.id.textInputFullName);

        bSignUp = findViewById(R.id.button_signUp);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    public void CreateAccount() {
        final String email = eEmail.getEditText().getText().toString().trim();
        final String password = ePassword.getEditText().getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please provide the needed information", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        /** User Verfication **/
                        mUser = mAuth.getCurrentUser();
                        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(CreateAccount.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        userProfile();
                        userData();
                        Toast.makeText(CreateAccount.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                        Toast.makeText(CreateAccount.this, "Verify Email to continue", Toast.LENGTH_LONG).show();

                        //Go back to login page
                        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

    }

    public void userData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Library").child("Users");

        String userEmail, userFullName, userUsername, userContact, userKey;
        userEmail = eEmail.getEditText().getText().toString().trim();
        userFullName = textInputFullName.getEditText().getText().toString().trim();
        userUsername = eUsername.getEditText().getText().toString().trim();
        userContact = textInputContact.getEditText().getText().toString().trim();

        userKey = mAuth.getCurrentUser().getUid();

        Users userRef = new Users();
        userRef.setFullName(userFullName);
        userRef.setUsername(userUsername);
        userRef.setEmail(userEmail);
        userRef.setContact(userContact);
        userRef.setUID(userKey);
        userRef.setUserType("Student");

        databaseReference.child(userKey).setValue(userRef);

    }

    public void userProfile() {
        String username = eUsername.getEditText().getText().toString().trim();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username).build();

            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateAccount.this, "Account Creation Success", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}