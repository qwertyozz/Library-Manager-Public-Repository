package com.example.customauthenticationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.UnknownServiceException;
import java.util.Date;
import java.util.EventListener;

public class SearchForBooksUpdated extends AppCompatActivity {

    //Views Reference variables
    private ImageButton buttonSearch;
    private EditText editTextSearch;
    private RecyclerView recyclerView;

    //Firebase DatabaseReference
    private DatabaseReference databaseReference, databaseReferenceDelete;

    // Firebase auth reference
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_books_updated);

        // Views reference variable values
        buttonSearch = findViewById(R.id.imageButtonSearch);
        editTextSearch = findViewById(R.id.editTextSearchBar);
        recyclerView = findViewById(R.id.recyclerviewResult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Firebase reference variable values
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceDelete = FirebaseDatabase.getInstance().getReference();

        /** Search Button for searching books using book title
         * invokes findBooks() which expects 1 argument(searchQueryUser) which gets the input of
         * the user of the app then processes the query **/
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQueryUser = editTextSearch.getText().toString();
                findBooks(searchQueryUser);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        findBooks("");
    }

    // method for finding books
    public void findBooks(String searchQuery){
        FirebaseRecyclerOptions<Books> options = new FirebaseRecyclerOptions.Builder<Books>()
                .setQuery(databaseReference.child("Library").child("Books").orderByChild("title")
                        .startAt(searchQuery).endAt(searchQuery + "\uf8ff"), Books.class)
                .build();

        FirebaseRecyclerAdapter<Books, BooksViewHolder> adapter = new
                FirebaseRecyclerAdapter<Books, BooksViewHolder>(options) {

                    /** Called by recyclerview to display the data at the specified position, this
                     method should update the contents of the RecyclerView.ViewHolder.ItemView to
                     reflect the item at the given position **/

                    @Override
                    protected void onBindViewHolder(@NonNull BooksViewHolder holder, final int position, @NonNull Books model) {
                        holder.title.setText(model.getTitle());
                        holder.author.setText("Author: " + model.getAuthor());
                        holder.publisher.setText("Publisher: " + model.getPublisher());
                        holder.year.setText("Year: " + model.getYear());
                        holder.genre.setText("Genre: " + model.getGenre());

                        /** deleteImageButton is used to delete a Book this requires administrative
                         * priveleges, if the ImageButton is clicked it will get the book title
                         * to display for the dialog box and the bookId is invoke for deleting because
                         * it is unique and to avoid deletion of other books with the same info.
                         * DeleteDialog() is also invoked which requires 2 arguments(bookTitle,
                         * bookId)**/

                        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String bookTitle = getItem(position).getTitle();
                                String bookId = getItem(position).getKey();

                                DeleteDialog(bookTitle, bookId);
                            }
                        });

                        /** borrowImageButton is used to borrow a book in the search tab, when clicked
                         * it will get the information of the book that will be borrowed and the
                         * information of the borrower.**/
                        holder.borrowImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // Book Borrowed Info
                                String bookTitle = getItem(position).getTitle();
                                String bookAuthor = getItem(position).getAuthor();
                                String bookPublisher = getItem(position).getPublisher();
                                String bookYear = getItem(position).getYear();

                                // Book Borrower's info
                                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                                String displayName = user.getDisplayName();
                                String email = user.getEmail();
                                String dateBorrowed = java.text.DateFormat.getDateTimeInstance()
                                        .format(new Date());
                                String borrowerKey = databaseReference.child("Library").child("Borrowed Books")
                                        .push().getKey();

                                /**
                                Query queryContact = databaseReference.child("Library").child("Users")
                                        .orderByChild("email").equalTo(email);

                                queryContact.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String contact = snapshot.getValue(String.class);
                                        System.out.println(contact);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                    **/
                                BorrowDialog(bookTitle, bookAuthor, bookPublisher, bookYear, displayName,
                                        email, dateBorrowed, borrowerKey);
                            }
                        });

                    }

                    /** Called when a RecyclerView needs a new RecyclerView.ViewHolder of the
                     given type to represent an item **/

                    @NonNull
                    @Override
                    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_layout, parent, false);
                        BooksViewHolder viewHolder = new BooksViewHolder(view);
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class BooksViewHolder extends RecyclerView.ViewHolder {

        TextView title, author, publisher, year, genre;
        ImageButton deleteImageButton, borrowImageButton;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewTitle);
            author = itemView.findViewById(R.id.textViewAuthor);
            publisher = itemView.findViewById(R.id.textViewPublisher);
            year = itemView.findViewById(R.id.textViewYear);
            genre = itemView.findViewById(R.id.textViewGenre);

            deleteImageButton = itemView.findViewById(R.id.imageButtonDelete);
            borrowImageButton = itemView.findViewById(R.id.imageButtonBorrow);

        }

    }

    // Delete Dialog when deleting a book.
    public void DeleteDialog(final String bookTitle, final String bookId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchForBooksUpdated.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete\n" + bookTitle + bookId + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query mquery = databaseReferenceDelete.child("Library").child("Books").orderByKey()
                        .equalTo(bookId);

                mquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();

                            Toast.makeText(SearchForBooksUpdated.this,
                                    "Book Successfully deleted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SearchForBooksUpdated.this, "Error Deleting",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    public void BorrowDialog(final String bookTitle, final String bookAuthor, final String bookPublisher, final String bookYear,
                             final String borrowerName, final String borrowerEmail, final String dateBorrowed, final String borrowerKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchForBooksUpdated.this);
        builder.setTitle("Borrow Book");
        builder.setMessage("BORROWER INFO " + "\n" + "Name: " + borrowerName + "\n" + "Email: " + borrowerEmail
                + "\n" + "Contact Number: "
                + "\n" + "Date: " + dateBorrowed + "\n" + "\n" + "BOOK INFO" + "\n" + "Title: " + bookTitle
                + "\n" + "Author: " + bookAuthor + "\n" + "Publisher: " + bookPublisher + "\n" + "Year: " +
                bookYear);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Library")
                        .child("Borrowed Books");

                BookBorrower bookBorrower = new BookBorrower(bookTitle, bookAuthor, bookPublisher,
                        bookYear, borrowerName, borrowerEmail, dateBorrowed, borrowerKey);

                databaseReference.child(borrowerKey).setValue(bookBorrower);

                Toast.makeText(SearchForBooksUpdated.this, "Book Borrowed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }
}