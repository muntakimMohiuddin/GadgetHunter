package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mks.gadgethunter.R;

public class ProductView extends AppCompatActivity {
    TextView productName;
    TextView productPrice;
    TextView productDescription;
    String productImageUrl;
    String productId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        Intent intent = getIntent();
        productImageUrl = intent.getStringExtra("ProductImageUrl");
        productId = intent.getStringExtra("ProductId");
        productName = (TextView) findViewById(R.id.product_name);
        productPrice = (TextView) findViewById(R.id.product_price);
        productDescription = (TextView) findViewById(R.id.product_specs);

    }
}
