package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mks.gadgethunter.Adapters.PostAdapter;
import com.example.mks.gadgethunter.Models.Post;
import com.example.mks.gadgethunter.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 06-Oct-17.
 */

public class Forum extends Fragment {

    RecyclerView rec;
    public static final int splashDuration = 10;
    PostAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> list;
    ArrayList<String> keyList = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_forum, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ForumPost.class);
                intent.putExtra("Admin Post", "false");
                startActivity(intent);
            }
        });

        initialize(rootview);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                adapter.list.add(0,new Post(p.uid, p.username, p.title, p.content, p.imageUrl, p.numberOfComments, p.postId));
                keyList.add(0,dataSnapshot.getKey().toString());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                adapter.list.set(keyList.indexOf(dataSnapshot.getKey()),
                        p);
                adapter.notifyDataSetChanged();
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
        return rootview;

    }

    private void initialize(View rootview) {
        rec = (RecyclerView) rootview.findViewById(R.id.forum_rec);
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new PostAdapter(list, getContext());
        rec.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Posts");

    }
}
