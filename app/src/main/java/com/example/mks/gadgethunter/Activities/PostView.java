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
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostView extends AppCompatActivity {
    TextView postContent;
    EditText commentEditText;
    ImageButton postButton;
    String postId;
    String postTitle;
    String postImageUrl;
    String postUid;
    ImageView postImage;
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
        setContentView(R.layout.activity_post_view);
        initialize();
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    postButton.setVisibility(View.VISIBLE);
                } else {
                    postButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        redundancy.add(postUid);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = commentEditText.getText().toString();
                commentEditText.setText("");
                postButton.setVisibility(View.INVISIBLE);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String commenterName = firebaseAuth.getCurrentUser().getDisplayName();
                Comments comments = new Comments(commenterName, commentContent,FirebaseAuth.getInstance().getCurrentUser().getUid());
                final int[] n = new int[1];
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
                     if(!postUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                         Notification notification = new Notification(FirebaseAuth.getInstance().getCurrentUser()
                                 .getDisplayName() + " commented on a post you were following titled:"+postTitle,
                                 postId);
                FirebaseDatabase.getInstance().getReference().child("Notifications").child(postUid)
                        .push().setValue(notification);
            }                FirebaseDatabase.getInstance().getReference().child("PostComments").child(postId).addChildEventListener(new ChildEventListener() {
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
            }
        });
        FirebaseDatabase.getInstance().getReference().child("PostComments").child(postId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Comments c = dataSnapshot.getValue(Comments.class);
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

    private void initialize() {

        postButton = (ImageButton) findViewById(R.id.post_button);
        postContent = (TextView) findViewById(R.id.post_content);
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        postUid=intent.getStringExtra("postUid");
        postTitle=intent.getStringExtra("postTitle");
        commentEditText = (EditText) findViewById(R.id.comment_edit_text);
        commentEditText.setMovementMethod(new ScrollingMovementMethod());
        postContent.setText(intent.getStringExtra("postContent") + "");
        postImage = (ImageView) findViewById(R.id.post_image);
        postImageUrl = intent.getStringExtra("postImageUrl");
        if (postImageUrl != null && postImageUrl.length() > 0) {
            Picasso.with(getApplicationContext()).load(postImageUrl).resize(900, 500).into(postImage);
        }
        recyclerView = (RecyclerView) findViewById(R.id.comment_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        list = new ArrayList<>();
        adapter = new CommentAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);

    }
}

