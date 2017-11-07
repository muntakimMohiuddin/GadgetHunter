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
 * Created by Kibria on 8/31/17.
 */

public class CustomAdapter extends ArrayAdapter {

    public static final String TAG = "MyTag";

    Context context;
    public ArrayList<Product> shopitem = new ArrayList<Product>();
    public ArrayList<Product> currentitem = new ArrayList<Product>();

    public CustomAdapter(Context context, ArrayList<Product> shopitem) {
        super(context, R.layout.item_template, R.id.product_name, shopitem);
        this.context = context;
        this.shopitem = shopitem;
    }


    class MyViewHolder {

        TextView productName;
        TextView productPrice;

        public MyViewHolder(View view) {

            productName = (TextView) view.findViewById(R.id.product_name);
            productPrice = (TextView) view.findViewById(R.id.product_price);
        }


    }


    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        MyViewHolder myViewHolder = null;
        if (row == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.item_template, parent, false);
            myViewHolder = new MyViewHolder(row);
            row.setTag(myViewHolder);
            Log.w(TAG, "creating new");
        } else {

            Log.i(TAG, "recycling stuff");
            myViewHolder = (MyViewHolder) row.getTag();
        }
        myViewHolder.productName.setText(shopitem.get(position).getProductName());
        myViewHolder.productPrice.setText(shopitem.get(position).getProductPrice() + " Tk.");
        return row;
    }

    /*private android.widget.Filter fRecords;

    //return the filter class object
    @Override
    public android.widget.Filter getFilter() {
        if (fRecords == null) {
            fRecords = new RecordFilter();
        }
        return fRecords;
    }

    //filter class
    private class RecordFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            //Implement filter logic
            // if edittext is null return the actual list
            if (constraint == null || constraint.length() == 0) {
                //No need for filter
                results.values = currentitem;
                results.count = currentitem.size();

            } else {
                //Need Filter
                // it matches the text  entered in the edittext and set the data in adapter list
                ArrayList<Product> fRecords = new ArrayList<Product>();

                for (Product s : currentitem) {
                    if (s.getProductName().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {
                        fRecords.add(s);
                    }
                }
                results.values = fRecords;
                results.count = fRecords.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            //it set the data from filter to adapter list and refresh the recyclerview adapter
            if(results.count==0) {
                notifyDataSetInvalidated();
            }
            else {
                shopitem = (ArrayList<Product>) results.values;
                notifyDataSetChanged();
            }
        }

        public boolean isLoggable(LogRecord record) {
            return false;
        }
    }*/

}

