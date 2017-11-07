package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mks.gadgethunter.Adapters.NotificationAdapter;
import com.example.mks.gadgethunter.Models.Notification;
import com.example.mks.gadgethunter.Models.Post;
import com.example.mks.gadgethunter.Models.Shops;
import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    ListView notificationListView;
    NotificationAdapter notificationAdapter;
    ArrayList<String> notificationList=new ArrayList<String>();
    ArrayList<String> postId=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        DatabaseReference mDatabaseRef=FirebaseDatabase.getInstance().getReference("Notifications")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        notificationListView= (ListView) findViewById(R.id.notification_list_view);
        notificationAdapter=new NotificationAdapter(this,notificationList);
        notificationListView.setAdapter(notificationAdapter);

        notificationList.clear();
        postId.clear();

        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Notification temp = dataSnapshot.getValue(Notification.class);
                notificationAdapter.notificationText.add(0,temp.getNotificationText());
                postId.add(0,temp.getPostId());
                notificationAdapter.notifyDataSetChanged();
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

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //Toast.makeText(this,"thsi",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "You clicked on item " + (i + 1), Toast.LENGTH_SHORT).show();
                Log.d("notification", postId.get(i) + " "+notificationAdapter.notificationText.get(i));
                FirebaseDatabase.getInstance().getReference().child("Posts").addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getKey().equals(postId.get(i))) ;
                                {
                                    Post item = dataSnapshot.getValue(Post.class);
                                    if (item.getPostId().equals(postId.get(i))) {
                                        Intent intent = new Intent(getApplicationContext(), PostView.class);
                                        intent.putExtra("postId", item.postId);
                                        intent.putExtra("postUid", item.uid);
                                        intent.putExtra("postContent", item.content);
                                        intent.putExtra("postImageUrl", item.imageUrl);
                                        intent.putExtra("postTitle",item.getTitle());
                                        startActivity(intent);
                                        finish();
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
                        }
                );
                FirebaseDatabase.getInstance().getReference().child("AdminPosts").addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getKey().equals(postId.get(i))) ;
                                {
                                    Post item =dataSnapshot.getValue(Post.class);
                                    if (item.getPostId().equals(postId.get(i))) {
                                        Intent intent = new Intent(getApplicationContext(), PostView.class);
                                        intent.putExtra("postId", item.postId);
                                        intent.putExtra("postUid", item.uid);
                                        intent.putExtra("postContent", item.content);
                                        intent.putExtra("postImageUrl", item.imageUrl);
                                        intent.putExtra("postTitle",item.getTitle());
                                        startActivity(intent);
                                        finish();
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
                        }
                );

            }
        });
    }
}
