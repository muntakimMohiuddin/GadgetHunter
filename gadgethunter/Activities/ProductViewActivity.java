package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mks.gadgethunter.Models.Product;
import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProductViewActivity extends AppCompatActivity {
    TextView productName;
    TextView productPrice;
    TextView productDescription;
    String productImageUrl;
    String productId;
    ImageView productImage;
    Button order;
    String shopId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Product product;
    String shopOwner;
    Button deleteProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        initialize();
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                intent.putExtra("product", product);
                intent.putExtra("shop id", shopId);
                startActivity(intent);
            }
        });
        if (product.getImageUrl().length() > 0)
            Picasso.with(getApplicationContext()).load(product.getImageUrl()).resize(800, 1200).into(productImage, new Callback() {
                @Override
                public void onSuccess() {
                    order.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {

                }
            });
        else
            order.setVisibility(View.VISIBLE);
        if(shopOwner.equals("true")){
            order.setVisibility(View.INVISIBLE);
            deleteProduct.setVisibility(View.VISIBLE);

        }

    }

    private void initialize() {
        Intent intent = getIntent();
        productImageUrl = intent.getStringExtra("ProductImageUrl");
        productId = intent.getStringExtra("ProductId");
        productName = (TextView) findViewById(R.id.product_name);
        productPrice = (TextView) findViewById(R.id.product_price);
        productDescription = (TextView) findViewById(R.id.product_specs);
        product = intent.getParcelableExtra("product");
        productName.setText(product.getProductName());
        productPrice.setText("Product Price: TK. " + product.getProductPrice());
        productDescription.setText(product.getProductDescription());
        productImage = (ImageView) findViewById(R.id.product_image);
        order = (Button) findViewById(R.id.order_button);
        shopId = intent.getStringExtra("shop id");
        shopOwner=intent.getStringExtra("shopOwner");
        Log.d("kib1",shopId);
        deleteProduct=(Button)findViewById(R.id.delete_button);
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Shops").child(FirebaseAuth
                        .getInstance().getCurrentUser().getUid()).child(product.getProductId()).removeValue();
                finish();
            }
        });


    }

}