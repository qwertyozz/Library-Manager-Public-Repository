package com.example.customauthenticationfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class UserMenu extends AppCompatActivity {

    //Views
    TextView Tusername;
    CardView addBooks, searchBooks, borrowBooks;

    //Firebase
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        Tusername = findViewById(R.id.textview_username);
        FirebaseUser user = mAuth.getInstance().getCurrentUser();

        if(user != null){
            String name = user.getDisplayName();
            Tusername.setText(name);
        }

        addBooks = findViewById(R.id.card_addBooks);
        searchBooks = findViewById(R.id.card_searchBooks);
        borrowBooks = findViewById(R.id.card_borrowBooks);

        addBooksMethod();
        searchBooksMethod();
        borrowBooks();


    }

    public void addBooksMethod(){
        addBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this,AddBooks.class);
                startActivity(intent);
            }
        });
    }

    public void searchBooksMethod(){
        searchBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this,SearchForBooksUpdated.class);
                startActivity(intent);
            }
        });
    }

    public void borrowBooks(){
        borrowBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this,BorrowedBooks.class);
                startActivity(intent);
            }
        });
    }
}