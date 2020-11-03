package com.example.customauthenticationfirebase;

import android.app.AlertDialog;
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

public class studentBorrowPending extends Fragment {

    // Views reference variables
    private ImageButton approve, disapprove, search;
    private TextView pendingName, pendingEmail, pendingBook, pendingAuthor;
    private EditText searchPending;
    private RecyclerView pendingRecyclerView;
    private TextView noDataPending;

    // Firebase Reference variables
    private DatabaseReference databaseReference, databaseReferenceDelete;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.activity_student_borrower_pending, container, false);

        //Views
        approve = v.findViewById(R.id.imageButton_approved);
        disapprove = v.findViewById(R.id.imageButton_disapproved);
        search = v.findViewById(R.id.imageButtonSearch);
        searchPending = v.findViewById(R.id.editTextSearchBar);
        pendingAuthor = v.findViewById(R.id.pending_author);
        pendingEmail = v.findViewById(R.id.pending_email);
        pendingName = v.findViewById(R.id.pending_name);
        pendingBook = v.findViewById(R.id.pending_bookTitle);
        noDataPending = v.findViewById(R.id.empty_student_view_pending);

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

        Query queryForPending = databaseReference.child("Library").child("Pending Borrow")
                .orderByChild("borrowerName")
                .startAt(searchQuery).endAt(searchQuery + "\uf8ff");

        final FirebaseRecyclerOptions<BookBorrower> options = new FirebaseRecyclerOptions
                .Builder<BookBorrower>()
                .setQuery(queryForPending,BookBorrower.class)
                .build();

        queryForPending.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    /** Called by recyclerview to display the data at the specified position, this
                     method should update the contents of the RecyclerView.ViewHolder.ItemView to
                     reflect the item at the given position **/

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


                                }

                                @NonNull
                                @Override
                                public PendingBorrowerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter_for_borrow_request,
                                            parent, false);
                                    PendingBorrowerHolder viewHolder = new PendingBorrowerHolder(view);
                                    return viewHolder;
                                }
                            };
                    pendingRecyclerView.setAdapter(adapter);
                    adapter.startListening();
                }else if(!snapshot.exists()){
                    noDataPending.setVisibility(View.VISIBLE);
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
}
