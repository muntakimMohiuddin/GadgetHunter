package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mks.gadgethunter.Adapters.FeaturedPostAdapter;
import com.example.mks.gadgethunter.Models.Post;
import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 04-Oct-17.
 */

public class Featured extends Fragment {
    RecyclerView rec;
    public static final int splashDuration = 10;
    FeaturedPostAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String noOfAdmins;
    ArrayList<String> admins;
    List<Post> list;
    int flagadmin = 0;
    static int flag = 0;
    ArrayList<String> keyList = new ArrayList<>();
    int i = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //  super.onCreate(savedInstanceState);
        View rootview = inflater.inflate(R.layout.fragment_featured, container, false);
        initialize(rootview);
        final FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        FirebaseDatabase.getInstance().getReference().child("Admin").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                admins.add(dataSnapshot.getValue(String.class));
                if (admins.indexOf(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()) != -1) {
                    flagadmin = 1;
                    fab.setVisibility(View.VISIBLE);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagadmin == 0)
                    Snackbar.make(view, "You have to be an admin to post here", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else {
                    Intent intent = new Intent(getContext(), ForumPost.class);
                    intent.putExtra("Admin Post", "true");
                    startActivity(intent);
                }
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                adapter.list.add(0,p);
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
        rec = (RecyclerView) rootview.findViewById(R.id.featured_rec);
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new FeaturedPostAdapter(list, getContext());
        rec.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("AdminPosts");
        admins = new ArrayList<>();
    }
}
