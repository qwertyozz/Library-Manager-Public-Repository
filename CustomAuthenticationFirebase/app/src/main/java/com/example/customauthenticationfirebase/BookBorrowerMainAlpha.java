package com.example.customauthenticationfirebase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.EventListener;


public class BookBorrowerMainAlpha extends Fragment {

    //Views reference variables
    private ImageButton buttonSearchForBorrow;
    private EditText editTextSearchForBorrow;
    private RecyclerView recyclerViewForBorrow;
    private TextView noData;

    //Firebase reference variables
    private DatabaseReference databaseReferenceForBorrow, dataRefReturn;
    private FirebaseAuth firebaseAuthForBorrow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_borrowed_books_alpha, container, false);

        //Views references
        noData = v.findViewById(R.id.empty_view);
        buttonSearchForBorrow = v.findViewById(R.id.imageButtonSearch);
        editTextSearchForBorrow = v.findViewById(R.id.editTextSearchBar);
        recyclerViewForBorrow = v.findViewById(R.id.recyclerviewResult);
        recyclerViewForBorrow.setHasFixedSize(true);
        recyclerViewForBorrow.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        findBorrowedBooks("");

        return v;
    }

    public void findBorrowedBooks(String searchQuery) {

        Query queryBorrowed = databaseReferenceForBorrow.child("Library").child("Borrowed Books").orderByChild("borrowerName")
                .startAt(searchQuery).endAt(searchQuery + "\uf8ff");

        final FirebaseRecyclerOptions<BookBorrower> options = new FirebaseRecyclerOptions.Builder<BookBorrower>()
                .setQuery(queryBorrowed, BookBorrower.class).build();

        queryBorrowed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    FirebaseRecyclerAdapter<BookBorrower, BookBorrowerViewHolder> adapter = new
                            FirebaseRecyclerAdapter<BookBorrower, BookBorrowerViewHolder>(options) {

                                @Override
                                protected void onBindViewHolder(@NonNull BookBorrowerViewHolder holder,
                                                                final int position, @NonNull final BookBorrower model) {

                                    /** Borrower Information **/
                                    holder.borrowerEmail.setText("Email: " + model.getBorrowerEmail());
                                    holder.borrowerName.setText("Name: " + model.getBorrowerName());
                                    holder.DateBorrowed.setText("Date Borrowed: " + model.getDateBorrower());

                                    /** Book Borrowed **/
                                    holder.borrowedTitle.setText("Title: " + model.getBookTitle());
                                    holder.borrowedAuthor.setText("Author: " + model.getBookAuthor());
                                    holder.borrowedPublisher.setText("Publisher: " + model.getBookPublisher());
                                    holder.borrowedDate.setText("Year: " + model.getBookYear());

                                    /** Return Button **/
                                    holder.returnButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Book Borrowed Info
                                            String bookTitle = model.getBookTitle();
                                            String bookAuthor = model.getBookAuthor();
                                            String bookkey = model.getBorrowerKey();

                                            // Borrower info
                                            String borrowerName = model.getBorrowerName();

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
                                            parent, false);
                                    BookBorrowerViewHolder viewHolder = new BookBorrowerViewHolder(view);
                                    return viewHolder;
                                }
                            };

                    recyclerViewForBorrow.setAdapter(adapter);
                    adapter.startListening();
                }else if(!snapshot.exists()){
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        /** Called by recyclerview to display the data at the specified position, this
         method should update the contents of the RecyclerView.ViewHolder.ItemView to
         reflect the item at the given position **/


    }

    public static class BookBorrowerViewHolder extends RecyclerView.ViewHolder {

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

    public void returnDialog(String Title, String Author, final String Key, String Name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();

                            Toast.makeText(getActivity(), "Book Successfully Returned",
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
