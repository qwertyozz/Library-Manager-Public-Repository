package com.example.customauthenticationfirebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.Date;

import org.w3c.dom.Text;

public class BorrowPending extends Fragment {

    // Views reference variables
    private ImageButton approve, disapprove, search;
    private TextView pendingName, pendingEmail, pendingBook, pendingAuthor;
    private EditText searchPending;
    private RecyclerView pendingRecyclerView;
    private TextView noPending;

    // Firebase Reference variables
    private DatabaseReference databaseReference, databaseReferenceDelete;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.activity_borrower_pending, container, false);

        //Views
        approve = v.findViewById(R.id.imageButton_approved);
        disapprove = v.findViewById(R.id.imageButton_disapproved);
        search = v.findViewById(R.id.imageButtonSearch);
        searchPending = v.findViewById(R.id.editTextSearchBar);
        pendingAuthor = v.findViewById(R.id.pending_author);
        pendingEmail = v.findViewById(R.id.pending_email);
        pendingName = v.findViewById(R.id.pending_name);
        pendingBook = v.findViewById(R.id.pending_bookTitle);
        noPending = v.findViewById(R.id.empty_view_pending);

        pendingRecyclerView = v.findViewById(R.id.recyclerviewPending);
        pendingRecyclerView.setHasFixedSize(true);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        approve = v.findViewById(R.id.imageButton_approved);
        disapprove = v.findViewById(R.id.imageButton_disapproved);

        //Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceDelete = FirebaseDatabase.getInstance().getReference();

        findPendingRequest("");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchPendingQuery = searchPending.getText().toString().trim();
                findPendingRequest(searchPendingQuery);
            }
        });

        return v;
    }

    public void findPendingRequest(String searchQuery){

        Query queryPending = databaseReference.child("Library").child("Pending Borrow").orderByChild("borrowerName")
                .startAt(searchQuery).endAt(searchQuery + "\uf8ff");

        final FirebaseRecyclerOptions<BookBorrower> options = new FirebaseRecyclerOptions
                .Builder<BookBorrower>()
                .setQuery(queryPending,BookBorrower.class)
                .build();

        /** Called by recyclerview to display the data at the specified position, this
         method should update the contents of the RecyclerView.ViewHolder.ItemView to
         reflect the item at the given position **/

        queryPending.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    FirebaseRecyclerAdapter<BookBorrower, PendingBorrowerHolder> adapter = new
                            FirebaseRecyclerAdapter<BookBorrower, PendingBorrowerHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull PendingBorrowerHolder holder, int position,
                                                                @NonNull final BookBorrower model) {

                                    /** Pending Information **/
                                    holder.pendingName.setText(model.getBorrowerName());
                                    holder.pendingAuthor.setText(model.getBookAuthor());
                                    holder.pendingBook.setText(model.getBookTitle());
                                    holder.pendingEmail.setText(model.getBorrowerEmail());

                                    holder.approve.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String bookTitle, bookAuthor, bookPublisher, bookYear, displayName,
                                                    email, dateBorrowed, borrowerKey;

                                            bookTitle = model.getBookTitle();
                                            bookAuthor = model.getBookAuthor();
                                            bookPublisher = model.getBookPublisher();
                                            bookYear = model.getBookYear();
                                            displayName = model.getBorrowerName();
                                            email = model.getBorrowerEmail();
                                            dateBorrowed = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                            borrowerKey = model.getBorrowerKey();

                                            String keyRemoval = model.getBorrowerKey();

                                            approveButton(bookTitle, bookAuthor, bookPublisher, bookYear, displayName,
                                                    email, dateBorrowed,borrowerKey, keyRemoval);

                                        }
                                    });

                                    holder.disapprove.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String keyRemovalDecline = model.getBorrowerKey();

                                            disapprovedButton(keyRemovalDecline);
                                        }
                                    });

                                }

                                @NonNull
                                @Override
                                public PendingBorrowerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_for_borrow_request,
                                            parent, false);
                                    PendingBorrowerHolder viewHolder = new PendingBorrowerHolder(view);
                                    return viewHolder;
                                }
                            };
                    pendingRecyclerView.setAdapter(adapter);
                    adapter.startListening();
                }else if(!snapshot.exists()){
                    noPending.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public static class PendingBorrowerHolder extends RecyclerView.ViewHolder{

        TextView pendingName, pendingEmail, pendingBook, pendingAuthor;

        ImageButton approve, disapprove;

        public PendingBorrowerHolder(View itemView){
            super(itemView);

            //Pending Things
            pendingName = itemView.findViewById(R.id.pending_name);
            pendingEmail =  itemView.findViewById(R.id.pending_email);
            pendingBook = itemView.findViewById(R.id.pending_bookTitle);
            pendingAuthor = itemView.findViewById(R.id.pending_author);

            // Image Buttons
            approve = itemView.findViewById(R.id.imageButton_approved);
            disapprove = itemView.findViewById(R.id.imageButton_disapproved);
        }

    }

    public void approveButton(final String pendingBookTitle, final String pendingAuthor, final String pendingBookPublisher,
                              final String pendingBookYear, final String pendingBorrowerName,
                              final String pendingBorrowerEmail, final String pendingDate,
                              final String pendingKey, final String keyForRemoval){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pending Request");
        builder.setMessage("BORROWER INFO " + "\n" + "Name: " + pendingBorrowerName + "\n" + "Email: " + pendingBorrowerEmail
                + "\n" + "Date: " + pendingDate + "\n" + "\n" + "BOOK INFO" + "\n" + "Title: " + pendingBookTitle
                + "\n" + "Author: " + pendingAuthor + "\n" + "Publisher: " + pendingBookPublisher + "\n" + "Year: " +
                pendingBookYear);

        builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Library")
                        .child("Borrowed Books");

                BookBorrower bookBorrower = new BookBorrower(pendingBookTitle, pendingAuthor, pendingBookPublisher,
                        pendingBookYear, pendingBorrowerName, pendingBorrowerEmail, pendingDate, pendingKey);

                databaseReference.child(pendingKey).setValue(bookBorrower);

                removeAfterApprove(keyForRemoval);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    public void removeAfterApprove(String key){
        Query query = databaseReferenceDelete
                .child("Library")
                .child("Pending Borrow")
                .orderByKey()
                .equalTo(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().removeValue();

                    Toast.makeText(getActivity(), "Request Approved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error Processing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void disapprovedButton(String key){
        Query query = databaseReferenceDelete
                .child("Library")
                .child("Pending Borrow")
                .orderByKey()
                .equalTo(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().removeValue();

                    Toast.makeText(getActivity(), "Request Declined", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error Processing", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
