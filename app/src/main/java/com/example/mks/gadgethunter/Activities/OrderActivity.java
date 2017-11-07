package com.example.mks.gadgethunter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mks.gadgethunter.Models.Order;
import com.example.mks.gadgethunter.Models.Product;
import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderActivity extends AppCompatActivity {
    String shopId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Product product;
    String quantity;
    String deliveryAddress;
    String paymentMethod;
    String contactInfo;
    TextView productName;
    EditText qty, pm, contInfo, dAdd;
    Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initialize();
        setListenners();

    }

    private void initialize() {
        qty = (EditText) findViewById(R.id.product_quantity);
        pm = (EditText) findViewById(R.id.payment_method);
        contInfo = (EditText) findViewById(R.id.contact_no);
        dAdd = (EditText) findViewById(R.id.delivery_address);
        orderButton = (Button) findViewById(R.id.order_button);
        productName = (TextView) findViewById(R.id.product_name);
        Intent intent = getIntent();
        product = intent.getParcelableExtra("product");
        Log.d("Product", product.toString());
        shopId = intent.getStringExtra("shop id");
        Log.d("ShopId", shopId);
        productName.setText(product.getProductName());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Orders").child(shopId);
    }

    private void setListenners() {
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderId = databaseReference.push().getKey();
                quantity = qty.getText().toString();
                paymentMethod = pm.getText().toString();
                deliveryAddress = dAdd.getText().toString();
                contactInfo = contInfo.getText().toString();
                Order order = new Order(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        orderId, product.productId, quantity, deliveryAddress, paymentMethod, contactInfo);
                databaseReference.child(orderId).setValue(order);
                Toast.makeText(getApplicationContext(),"Your order has been placed",Toast.LENGTH_SHORT);
                finish();
            }
        });
        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    int price = Integer.parseInt(s.toString());
                    price *= Integer.parseInt(product.productPrice);
                    TextView textView = (TextView) findViewById(R.id.product_price);
                    textView.setText(String.valueOf(price));
                } else {
                    TextView textView = (TextView) findViewById(R.id.product_price);
                    textView.setText(String.valueOf(0));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
