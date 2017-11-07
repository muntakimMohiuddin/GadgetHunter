package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mks.gadgethunter.Adapters.CustomAdapter;
import com.example.mks.gadgethunter.Adapters.ShopAdapter;
import com.example.mks.gadgethunter.Models.Product;
import com.example.mks.gadgethunter.Models.Shops;
import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewShop extends Fragment {

    ListView shopProductList;
    TextView shopName;
    EditText search;
    String shopId, shopname;
    ArrayList<Product> itemList = new ArrayList<Product>();
    ArrayList<Product> current = new ArrayList<Product>();
    CustomAdapter customAdapter;
    List<String> keyList;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.activity_view_shop, container, false);

        shopId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        shopname = new String();
        shopName = (TextView) rootview.findViewById(R.id.shop_name);
        keyList=new ArrayList<>();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Shops").child(shopId);
        //Referencing the firebase database
        itemList.clear();
        shopProductList = (ListView) rootview.findViewById(R.id.shop_item_name_list);
        search = (EditText) rootview.findViewById(R.id.search_item);
        customAdapter = new CustomAdapter(getContext(), itemList);
        shopProductList.setAdapter(customAdapter);

        FirebaseDatabase.getInstance().getReference().child("ShopName").child(shopId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                shopname = dataSnapshot.getValue(String.class);
                shopName.setText(shopname);
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
                ViewShop.this.customAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = search.getText().length();
                current.clear();
                for (int i = 0; i < itemList.size(); i++) {
                    if (textlength <= itemList.get(i).getProductName().length()) {
                        // Log.d("ertyyy",itemList.get(i).getMovieName().toLowerCase().trim());
                        if (itemList.get(i).getProductName().toLowerCase().trim().contains(
                                search.getText().toString().toLowerCase().trim())) {
                            current.add(itemList.get(i));
                        }
                    }
                    customAdapter = new CustomAdapter(getContext(), current);
                    shopProductList.setAdapter(customAdapter);
                    //ShopItemActivity.this.customAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //Fetching Shop item list
        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product temp = dataSnapshot.getValue(Product.class);
                customAdapter.shopitem.add(temp);
                current.add(temp);
                keyList.add(dataSnapshot.getKey());
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                customAdapter.shopitem.remove(keyList.indexOf(dataSnapshot.getKey()));
                current.remove(keyList.indexOf(dataSnapshot.getKey()));
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Click on an item and the description of that item is shown
        shopProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "You clicked on item " + (i + 1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ProductViewActivity.class);
                intent.putExtra("product", current.get(i));
                intent.putExtra("shop id", shopId);
                intent.putExtra("shopOwner","true");
                startActivity(intent);
                Log.d("Kib", i + " ");
            }
        });
        return rootview;
    }


}