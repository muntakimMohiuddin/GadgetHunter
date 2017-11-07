package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mks.gadgethunter.Adapters.CommentAdapter;
import com.example.mks.gadgethunter.Models.Comments;
import com.example.mks.gadgethunter.Models.Notification;
import com.example.mks.gadgethunter.Models.Post;
import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    EditText comment;
    ImageButton commentButton;
    String postId;
    String postUid;
    String postTitle;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Comments> list;
    CommentAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference commentReference;
    ArrayList<String> keyList = new ArrayList<>();
    ArrayList<String> redundancy=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        intialize();
        commentButton.setVisibility(View.INVISIBLE);
        redundancy.add(postUid);
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    commentButton.setVisibility(View.VISIBLE);
                } else {
                    commentButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = comment.getText().toString();
                comment.setText("");
                commentButton.setVisibility(View.INVISIBLE);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                commentReference = firebaseDatabase.getReference();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String commenterName = firebaseAuth.getCurrentUser().getDisplayName();
                Comments comments = new Comments(commenterName, commentContent,
                        FirebaseAuth.getInstance().getCurrentUser().getUid());
                Log.d("post and comment", "ok");
                databaseReference.child("PostComments").child(postId).push().setValue(comments);
                databaseReference.child("AdminPosts").child(postId).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals("numberOfComments")) {
                            int i = Integer.parseInt(dataSnapshot.getValue(String.class));
                            databaseReference.child("AdminPosts").child(postId).child("numberOfComments").setValue("" + (i + 1));
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!postUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Notification notification = new Notification(FirebaseAuth.getInstance().getCurrentUser()
                            .getDisplayName() + " commented on a post you were following titled:"+postTitle,
                            postId);
                    FirebaseDatabase.getInstance().getReference().child("Notifications").child(postUid)
                            .push().setValue(notification);
                }
                FirebaseDatabase.getInstance().getReference().child("PostComments").child(postId)
                        .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Comments c = dataSnapshot.getValue(Comments.class);
                        if (redundancy.indexOf(c.commenterUid) == -1) {
                            if (!c.commenterUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                redundancy.add(c.getCommenterUid());
                                Notification notification = new Notification(FirebaseAuth.getInstance().getCurrentUser()
                                        .getDisplayName() + " commented on a post you were following titled:"+postTitle,
                                        postId);
                                FirebaseDatabase.getInstance().getReference().child("Notifications").child(c.commenterUid)
                                        .push().setValue(notification);
                            }
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                databaseReference.child("Posts").child(postId).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals("numberOfComments")) {
                            int i = Integer.parseInt(dataSnapshot.getValue(String.class));
                            databaseReference.child("Posts").child(postId).child("numberOfComments").setValue("" + (i + 1));

                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////
        Log.d("before rec c captured", "" + String.valueOf(FirebaseDatabase.getInstance().getReference() == null));

        FirebaseDatabase.getInstance().getReference().child("PostComments")
                .child(postId).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("rec child", "ok");
                Comments c = dataSnapshot.getValue(Comments.class);
                Log.d("rec comment captured", "ok");
                adapter.list.add(c);

                keyList.add(dataSnapshot.getKey().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void intialize() {
        comment = (EditText) findViewById(R.id.comment_edit_text);
        comment.setMovementMethod(new ScrollingMovementMethod());
        commentButton = (ImageButton) findViewById(R.id.post_button);
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        postUid=intent.getStringExtra("postUid");
        postTitle=intent.getStringExtra("postTitle");
        Log.d("rec st", "ok");
        ///////////////////////////////////////////////////////
        recyclerView = (RecyclerView) findViewById(R.id.comment_recycle_view);
        Log.d("rec initialized", "ok");
        recyclerView.setHasFixedSize(true);
        Log.d("rec sized", "ok");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Log.d("rec layout set", "ok");
        list = new ArrayList<>();
        adapter = new CommentAdapter(list, getApplicationContext());
        Log.d("rec adapter initialized", "ok");
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        Log.d("rec adapter set", "ok");


    }
}
