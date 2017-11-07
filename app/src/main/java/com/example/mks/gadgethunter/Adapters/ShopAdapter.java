package com.example.mks.gadgethunter.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mks.gadgethunter.Models.Product;
import com.example.mks.gadgethunter.R;

import java.util.ArrayList;

/**
 * Created by kibria on 02/11/17.
 */

public class ShopAdapter extends ArrayAdapter {

    private static final String TAG = "MyTag";

    Context context;
    public ArrayList<String> shopname = new ArrayList<String>();
    public ArrayList<Product> currentitem = new ArrayList<Product>();

    public ShopAdapter(Context context, ArrayList<String> shopname) {
        super(context, R.layout.shop_template, R.id.shop_name, shopname);
        this.context = context;
        this.shopname = shopname;
    }


    class MyViewHolder {

        TextView shopName;

        public MyViewHolder(View view) {

            shopName = (TextView) view.findViewById(R.id.shop_name);
        }


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        MyViewHolder myViewHolder = null;
        if (row == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.shop_template, parent, false);
            myViewHolder = new MyViewHolder(row);
            row.setTag(myViewHolder);
            Log.w(TAG, "creating new");
        } else {

            Log.i(TAG, "recycling stuff");
            myViewHolder = (MyViewHolder) row.getTag();
        }

        myViewHolder.shopName.setText(shopname.get(position));

        return row;
    }
}
