package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mks.gadgethunter.Adapters.CustomAdapter;
import com.example.mks.gadgethunter.Models.Product;
import com.example.mks.gadgethunter.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShopItemActivity extends AppCompatActivity {

    ListView shopProductList;
    TextView shopName;
    EditText search;
    String shopId, shopname;

    ArrayList<Product> itemList = new ArrayList<Product>();
    ArrayList<Product> current = new ArrayList<Product>();
    CustomAdapter customAdapter;
    String shopOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item);
        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");
        shopname = intent.getStringExtra("shopName");
        shopOwner=intent.getStringExtra("shopOwner");
        Log.d("$00",shopId);
        shopName = (TextView) findViewById(R.id.shop_name);
        shopName.setText(shopname);
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Shops").child(shopId);
        //Referencing the firebase database
        itemList.clear();
        shopProductList = (ListView) findViewById(R.id.shop_item_name_list);
        search = (EditText) findViewById(R.id.search_item);
        customAdapter = new CustomAdapter(this, itemList);
        shopProductList.setAdapter(customAdapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ShopItemActivity.this.customAdapter.getFilter().filter(s);
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
                    customAdapter = new CustomAdapter(ShopItemActivity.this, current);
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
                customAdapter.notifyDataSetChanged();
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

        //Click on an item and the description of that item is shown
        shopProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "You clicked on item " + (i + 1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ProductViewActivity.class);
                intent.putExtra("product", current.get(i));
                intent.putExtra("shop id", shopId);
                intent.putExtra("shopOwner",shopOwner);
                startActivity(intent);

            }
        });
        shopProductList.setLongClickable(true);
        shopProductList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("#0000000",shopOwner);
                if(shopOwner.equals("true")){

                    Toast.makeText(view.getContext(),"ok",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
                return true;
            }

        });

    }


}
