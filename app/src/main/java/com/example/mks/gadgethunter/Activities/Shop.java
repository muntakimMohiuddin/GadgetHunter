package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mks.gadgethunter.Adapters.ShopAdapter;
import com.example.mks.gadgethunter.Models.Shops;
import com.example.mks.gadgethunter.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Shop extends Fragment {

    ArrayList<String> shopList = new ArrayList<String>();
    ArrayList<String> shopId = new ArrayList<String>();

    ArrayList<String> currentName = new ArrayList<String>();
    ArrayList<String> currentId = new ArrayList<String>();
    ArrayList<String> ketList=new ArrayList<>();
    ShopAdapter adapter;
    EditText search;
    ListView shopName;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_shop, container, false);
        search = (EditText) rootview.findViewById(R.id.search_shop);
        shopName = (ListView) rootview.findViewById(R.id.shop_name_list);
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("ShopName");
        adapter = new ShopAdapter(getContext(), shopList);
        shopName.setAdapter(adapter);
        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String shopid = dataSnapshot.getKey();
                if (ketList.indexOf(shopid)==-1){
                Log.d("Shop", shopid);
                Shops temp = dataSnapshot.getValue(Shops.class);
                adapter.add(temp.getShopName());
                currentName.add(temp.getShopName());
                currentId.add(shopid);
                shopId.add(shopid);
                ketList.add(shopid);
                adapter.notifyDataSetChanged();
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


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Shop.this.adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = search.getText().length();
                currentName.clear();
                currentId.clear();
                for (int i = 0; i < shopList.size(); i++) {
                    if (textlength <= shopList.get(i).length()) {
                        // Log.d("ertyyy",itemList.get(i).getMovieName().toLowerCase().trim());
                        if (shopList.get(i).toLowerCase().trim().contains(
                                search.getText().toString().toLowerCase().trim())) {
                            currentName.add(shopList.get(i));
                            currentId.add(shopId.get(i));
                        }
                    }
                    adapter = new ShopAdapter(getContext(), currentName);
                    shopName.setAdapter(adapter);
                    //ShopItemActivity.this.customAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        shopName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "You clicked on item " + (i + 1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ShopItemActivity.class);
                intent.putExtra("shopOwner","false");
                intent.putExtra("shopId", currentId.get(i));
                intent.putExtra("shopName", currentName.get(i));
                startActivity(intent);
                Log.d("Kib", i + " ");
            }
        });
        return rootview;
    }
}
