package com.example.customauthenticationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BorrowedBooks extends AppCompatActivity {

    //Views reference variables
    private ImageButton buttonSearchForBorrow;
    private EditText editTextSearchForBorrow;
    private RecyclerView recyclerViewForBorrow;

    //Firebase reference variables
    private DatabaseReference databaseReferenceForBorrow, dataRefReturn;
    private FirebaseAuth firebaseAuthForBorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_books);

        // Views reference
        buttonSearchForBorrow = findViewById(R.id.imageButtonSearch);
        editTextSearchForBorrow = findViewById(R.id.editTextSearchBar);
        recyclerViewForBorrow = findViewById(R.id.recyclerviewResult);
        recyclerViewForBorrow.setHasFixedSize(true);
        recyclerViewForBorrow.setLayoutManager(new LinearLayoutManager(this));

        // Firebase reference
        databaseReferenceForBorrow = FirebaseDatabase.getInstance().getReference();
        dataRefReturn = FirebaseDatabase.getInstance().getReference();

        buttonSearchForBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryOfUser = editTextSearchForBorrow.getText().toString().trim();
                findBorrowedBooks(queryOfUser);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        findBorrowedBooks("");
    }

    public void findBorrowedBooks(String searchQuery){

        FirebaseRecyclerOptions<BookBorrower> options = new FirebaseRecyclerOptions.Builder<BookBorrower>()
                .setQuery(databaseReferenceForBorrow.child("Library").child("Borrowed Books").orderByChild("bookTitle")
                .startAt(searchQuery).endAt(searchQuery + "\uf8ff"),BookBorrower.class).build();

        FirebaseRecyclerAdapter<BookBorrower, BookBorrowerViewHolder> adapter = new
                FirebaseRecyclerAdapter<BookBorrower, BookBorrowerViewHolder>(options) {

                    /** Called by recyclerview to display the data at the specified position, this
                     method should update the contents of the RecyclerView.ViewHolder.ItemView to
                     reflect the item at the given position **/

            @Override
            protected void onBindViewHolder(@NonNull BookBorrowerViewHolder holder, final int position, @NonNull BookBorrower model) {

                /** Borrower Information **/
                holder.borrowerEmail.setText("Email: " + model.getBorrowerEmail());
                holder.borrowerName.setText("Name: " + model.getBorrowerName());
                holder.DateBorrowed.setText("Date Borrowed: " + model.getDateBorrower());

                /** Book Borrowed **/
                holder.borrowedTitle.setText("Title: " + model.getBookTitle());
                holder.borrowedAuthor.setText("Author: " + model.getBookAuthor());
                holder.borrowedPublisher.setText("Publisher: " + model.getBookPublisher());
                holder.borrowedDate.setText("Date: " + model.getBookYear());

                /** Return Button **/
                holder.returnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Book Borrowed Info
                        String bookTitle = getItem(position).getBookTitle();
                        String bookAuthor = getItem(position).getBookAuthor();
                        String bookkey = getItem(position).getBorrowerKey();

                        // Borrower info
                        String borrowerName = getItem(position).getBorrowerName();

                        returnDialog(bookTitle, bookAuthor, bookkey, borrowerName);
                    }
                });
            }

                    /** Called when a RecyclerView needs a new RecyclerView.ViewHolder of the
                     given type to represent an item **/

            @NonNull
            @Override
            public BookBorrowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_layout_forborrowedbooks,
                        parent,false);
                BookBorrowerViewHolder viewHolder = new BookBorrowerViewHolder(view);
                return viewHolder;
            }
        };

        recyclerViewForBorrow.setAdapter(adapter);
        adapter.startListening();
    }

    public static class BookBorrowerViewHolder extends RecyclerView.ViewHolder{

        TextView borrowedTitle, borrowedAuthor, borrowedPublisher, borrowedDate, borrowerName,
        borrowerEmail, DateBorrowed;

        ImageButton returnButton;

        public BookBorrowerViewHolder(@NonNull View itemView) {
            super(itemView);

            // Book Borrowed
            borrowedTitle = itemView.findViewById(R.id.textViewBorrowedtitle);
            borrowedAuthor = itemView.findViewById(R.id.textViewBorrowedAuthor);
            borrowedPublisher = itemView.findViewById(R.id.textViewBorrowedPublisher);
            borrowedDate = itemView.findViewById(R.id.textViewBorrowedYear);

            // Borrower Information
            borrowerName = itemView.findViewById(R.id.textViewNameOfBorrower);
            borrowerEmail = itemView.findViewById(R.id.textViewEmailOfBorrower);
            DateBorrowed = itemView.findViewById(R.id.textViewDateBorrowed);

            // return button
            returnButton = itemView.findViewById(R.id.imageButtonReturn);
        }
    }

    public void returnDialog(String Title, String Author, final String Key, String Name){
        AlertDialog.Builder builder = new AlertDialog.Builder(BorrowedBooks.this);
        builder.setTitle("Return Book");
        builder.setMessage("Return this book?" + "\n"
        + "Title: " + Title + "\n"
        + "Author: " + Author + "\n"
        + "Borrower Name: " + Name + "\n"
        + "Key: " + Key);

        builder.setPositiveButton("Return", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query returnQuery = dataRefReturn.child("Library").child("Borrowed Books")
                        .orderByKey().equalTo(Key);

                returnQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();

                            Toast.makeText(BorrowedBooks.this, "Book Successfully Returned",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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