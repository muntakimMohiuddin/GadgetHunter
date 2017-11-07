package com.example.mks.gadgethunter.Activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mks.gadgethunter.Adapters.FeaturedPostAdapter;
import com.example.mks.gadgethunter.Adapters.OrdersAdapter;
import com.example.mks.gadgethunter.Models.Order;
import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrders extends Fragment {
    RecyclerView rec;
    OrdersAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Order> list;
    List<String> keyList;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_fragment_orders, container, false);
        initialize(rootView);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                adapter.list.add(order);
                keyList.add(dataSnapshot.getKey().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);
                adapter.list.remove(keyList.indexOf(dataSnapshot.getKey()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;

    }

    private void initialize(View rootView) {
        rec = (RecyclerView) rootView.findViewById(R.id.orders_rec);
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        rec.setNestedScrollingEnabled(false);
        list = new ArrayList<>();
        keyList = new ArrayList<>();
        adapter = new OrdersAdapter(list, getContext());
        rec.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Orders").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid());

    }
}
