package com.example.mks.gadgethunter.shuvo.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mks.gadgethunter.R;
import com.example.mks.gadgethunter.shuvo.ui.adapters.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {

    ListView messagesListView;
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        ArrayList<String> user=new ArrayList<String>();
        ArrayList<String> lastMessages=new ArrayList<String>();
        messagesListView= (ListView) findViewById(R.id.user_messages_list_view);
        MessagesAdapter messagesAdapter=new MessagesAdapter(this,user,lastMessages);
        messagesListView.setAdapter(messagesAdapter);
        mDatabase.child("UserMessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //For showing current user's Message list
        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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


        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(this,"thsi",Toast.LENGTH_SHORT).show();
                Log.d("Kib",i+" ");
            }
        });
    }
}
