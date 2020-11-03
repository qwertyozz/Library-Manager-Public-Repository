/*
*DEPENDENCY implementation 'com.firebaseui:firebase-ui-database:0.4.0'
package com.example.customauthenticationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.customauthenticationfirebase.Books;
import com.example.customauthenticationfirebase.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

public class SearchForBooks extends AppCompatActivity {

    //Views
    private RecyclerView recyclerView;
    private EditText searchBar;
    private ImageButton imageButton, imageButtonDelete;

    //Database
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_books);

        recyclerView = findViewById(R.id.recyclerviewResult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchBar = findViewById(R.id.editTextSearchBar);
        imageButton = findViewById(R.id.imageButtonSearch);
        imageButtonDelete = findViewById(R.id.imageButtonDelete);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books").child("Library");
        databaseReference.keepSynced(true);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getQuery = searchBar.getText().toString();
                findBooks(getQuery);
            }
        });
        
    }

    private void findBooks(String searchText){

        Query firebaseSearchQuery = databaseReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Books,BooksViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Books, BooksViewHolder>
                (Books.class,R.layout.adapter_view_layout,BooksViewHolder.class,firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(BooksViewHolder booksViewHolder, Books books, int i) {
                booksViewHolder.setTitle(books.getTitle());
                booksViewHolder.setAuthor(books.getAuthor());
                booksViewHolder.setPublisher(books.getPublisher());
                booksViewHolder.setYear(books.getGenre());
                booksViewHolder.setGenre(books.getGenre());

            }

        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BooksViewHolder extends RecyclerView.ViewHolder{
        View view;
        public BooksViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }
        public void setTitle(String Title){
            TextView titleDisplay = view.findViewById(R.id.textViewTitle);
            titleDisplay.setText(Title);
        }
        public void setAuthor(String Author){
            TextView authorDisplay = view.findViewById(R.id.textViewAuthor);
            authorDisplay.setText(Author);
        }
        public void setPublisher(String Publisher){
            TextView publisherDisplay = view.findViewById(R.id.textViewPublisher);
            publisherDisplay.setText(Publisher);
        }
        public void setYear(String Year){
            TextView yearDisplay = view.findViewById(R.id.textViewYear);
            yearDisplay.setText(Year);
        }
        public void setGenre(String Genre){
            TextView genreDisplay = view.findViewById(R.id.textViewGenre);
            genreDisplay.setText(Genre);
        }

    }

    public void deleteMethod(int position){

    }
} */