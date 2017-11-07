package com.example.mks.gadgethunter.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mks.gadgethunter.Models.Order;
import com.example.mks.gadgethunter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Muntakim on 04-Nov-17.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    public List<Order> list;
    public Context context;


    public OrdersAdapter(List<Order> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_cards, parent, false);
        return new OrdersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order item = list.get(position);
        holder.contactNo.setText(item.getContactInfo());
        holder.deliveryAddress.setText(item.getDeliveryAddress());
        holder.ordererName.setText(item.getOrderer());
        holder.paymentMethod.setText(item.getPaymentMethod());
        holder.productQuantity.setText("Qty: " + item.getQuantity());
        holder.contactNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = item.getContactInfo().toString().replaceAll("-", "");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_no));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                v.getContext().startActivity(callIntent);
            }
        });

        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Orders").child(
                        FirebaseAuth.getInstance().getCurrentUser().getUid()
                ).child(item.getOrderid()).removeValue();
                Log.d("Rm order", item.getOrderid());
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Shops").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child(item.getProductId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("productPrice")) {
                   try {


                       int price = Integer.parseInt(dataSnapshot.getValue(String.class));
                       price *= Integer.parseInt(item.getQuantity());
                       holder.productPrice.setText("Price: Tk." + price);
                   }
                   catch (Exception e){

                   }
                } else if (dataSnapshot.getKey().equals("productName")) {
                    holder.productName.setText(dataSnapshot.getValue(String.class));
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView delivered, ordererName, deliveryAddress, productQuantity, productPrice, paymentMethod, contactNo, productName;

        public ViewHolder(View itemView) {
            super(itemView);
            delivered = (TextView) itemView.findViewById(R.id.delivered);
            ordererName = (TextView) itemView.findViewById(R.id.orderer_name);
            deliveryAddress = (TextView) itemView.findViewById(R.id.delivery_address);
            productQuantity = (TextView) itemView.findViewById(R.id.product_quantity);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            paymentMethod = (TextView) itemView.findViewById(R.id.payment_method);
            contactNo = (TextView) itemView.findViewById(R.id.contact_no);
            productName = (TextView) itemView.findViewById(R.id.product_name);
        }
    }
}

