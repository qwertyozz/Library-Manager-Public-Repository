package com.example.customauthenticationfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    // TextViews
    TextView userName;

    //Cardviews
    CardView search, add, borrowed, about;

    //ImageButton
    ImageButton signOut;

    // Firebase

    FirebaseAuth firebaseAuth;

    // Variable String
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        search = findViewById(R.id.cardview_Search);
        add = findViewById(R.id.cardview_Add);
        borrowed = findViewById(R.id.cardview_borrowed);
        about = findViewById(R.id.cardview_About);

        userName = findViewById(R.id.textView_username_output);

        signOut = findViewById(R.id.imageButtonSignOut);

        name = firebaseAuth.getInstance().getCurrentUser().getDisplayName();

        SearchForBooks();
        AddBooks();
        BorrowedBooks();
        AboutDevs();

        SignOut();

    }

    @Override
    protected void onStart() {
        super.onStart();
        userName.setText("Welcome back " + name + "!");
    }

    public void SearchForBooks(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(Dashboard.this,SearchForBooksUpdated.class);
                startActivity(searchIntent);
            }
        });
    }

    public void AddBooks(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(Dashboard.this,AddBooks.class);
                startActivity(addIntent);
            }
        });
    }

    public void BorrowedBooks(){
        borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent borrowedBooks = new Intent(Dashboard.this,BorrowedBooks.class);
                startActivity(borrowedBooks);
            }
        });
    }

    public void AboutDevs(){
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Under Maintenance", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SignOut(){
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(Dashboard.this, "Signed Out", Toast.LENGTH_SHORT).show();
            }
        });
    }
}