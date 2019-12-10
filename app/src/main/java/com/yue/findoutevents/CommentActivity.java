package com.yue.findoutevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private EditText mEditTextComment;
    private CommentAdapter commentAdapter;
    private Button mCommentSubmitButton;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
//        final String eventId = intent.getStringExtra("EventID");
        final String eventId = intent.getStringExtra(Utils.COMMENT_KEY);


        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        mEditTextComment = (EditText) findViewById(R.id.comment_edittext);
        mCommentSubmitButton = (Button) findViewById(R.id.comment_submit);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        commentAdapter = new CommentAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getData(eventId, commentAdapter);


        mCommentSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment(eventId);
                mEditTextComment.setText("");
                getData(eventId, commentAdapter);

            }
        });

    }


    private void sendComment(final String eventId) {
        String description = mEditTextComment.getText().toString();
        if (description.equals("")) {
            return;
        }
        Comment comment = new Comment();
        comment.setCommenter(Utils.username);
        comment.setEventId(eventId);
        comment.setDescription(description);
        comment.setTime(System.currentTimeMillis());
        String key = mDatabaseReference.child("comments").push().getKey();
        comment.setCommentId(key);
        mDatabaseReference.child("comments").child(key).setValue(comment, new DatabaseReference.
                CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "The comment is failed," +
                            " please check you network status.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "The comment is reported", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }



    private void getData(final String eventId, final CommentAdapter commentAdapter) {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot commentSnapshot = dataSnapshot.child("comments");
                List<Comment> comments = new ArrayList<Comment>();
                for (DataSnapshot noteDataSnapshot : commentSnapshot.getChildren()) {
                    Comment comment = noteDataSnapshot.getValue(Comment.class);
                    if(comment.getEventId().equals(eventId)) {
                        comments.add(comment);
                    }
                }
                mDatabaseReference.getRef().child("events").child(eventId).
                        child("commentNumber").setValue(comments.size());
                commentAdapter.setComments(comments);


                DataSnapshot eventSnapshot = dataSnapshot.child("events");
                for (DataSnapshot noteDataSnapshot : eventSnapshot.getChildren()) {
                    Event event = noteDataSnapshot.getValue(Event.class);
                    if(event.getId().equals(eventId)) {
                        commentAdapter.setEvent(event);
                        break;
                    }
                }
                if (mRecyclerView.getAdapter() != null) {
                    commentAdapter.notifyDataSetChanged();
                } else {
                    mRecyclerView.setAdapter(commentAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
