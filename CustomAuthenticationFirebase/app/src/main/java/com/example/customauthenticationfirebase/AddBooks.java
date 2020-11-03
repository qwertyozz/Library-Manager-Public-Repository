package com.example.customauthenticationfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.security.interfaces.ECPublicKey;

public class AddBooks extends AppCompatActivity {

    //Views reference variables
    TextInputLayout Etitle, Eauthor, Epublisher, Eyear, Egenre;
    Button Badd, Bclear;

    //Firebase reference variables
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    //Java Class
    Books books;

    //getting text
    String title, author, publisher, year, genre;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        Etitle = findViewById(R.id.textInputLayoutTitle);
        Eauthor = findViewById(R.id.textInputLayoutAuthor);
        Epublisher = findViewById(R.id.textInputLayoutPublisher);
        Eyear = findViewById(R.id.textInputLayoutYear);
        Egenre = findViewById(R.id.textInputLayoutGenre);


        Badd = findViewById(R.id.button_add);
        Bclear = findViewById(R.id.button_clear);

        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = Etitle.getEditText().getText().toString().trim();
                author = Eauthor.getEditText().getText().toString().trim();
                publisher = Epublisher.getEditText().getText().toString().trim();
                year = Eyear.getEditText().getText().toString().trim();
                genre = Egenre.getEditText().getText().toString().trim();

                confirmation();
            }
        });

        Bclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

    }

    public void clear(){
        Etitle.getEditText().setText(null);
        Eauthor.getEditText().setText(null);
        Epublisher.getEditText().setText(null);
        Eyear.getEditText().setText(null);
        Egenre.getEditText().setText(null);
    }

    public void confirmation(){
        if(!titleValidation() | !authorValidation() | !publisherValidation() | !yearValidation()
                | !genreValidation()){
            return;
        }else{
            myRef = database.getReference().child("Library").child("Books");
            books = new Books();

            books.setTitle(title);
            books.setAuthor(author);
            books.setPublisher(publisher);
            books.setYear(year);
            books.setGenre(genre);

            String key = database.getReference().child("Library").child("Books").push().getKey();
            books.setKey(key);

            myRef.child(key).setValue(books);

            Toast.makeText(AddBooks.this, "Book Saved", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean titleValidation(){
        if(title.isEmpty()){
            Etitle.setError("Name is required");
            return false;
        }else{
            Etitle.setError(null);
            return true;
        }
    }

    public boolean authorValidation(){
        if(author.isEmpty()){
            Eauthor.setError("Author is required");
            return false;
        }else{
            Eauthor.setError(null);
            return true;
        }
    }

    public boolean publisherValidation(){
        if(publisher.isEmpty()){
            Epublisher.setError("Publisher is required");
            return false;
        }else{
            Epublisher.setError(null);
            return true;
        }
    }

    public boolean yearValidation(){
        if(year.isEmpty()){
            Eyear.setError("Year is required");
            return false;
        }else{
            Eyear.setError(null);
            return true;
        }
    }

    public boolean genreValidation(){
        if(genre.isEmpty()){
            Egenre.setError("Genre is required");
            return false;
        }else{
            Egenre.setError(null);
            return true;
        }
    }

}