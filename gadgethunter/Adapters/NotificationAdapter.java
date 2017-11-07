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

import com.example.mks.gadgethunter.R;

import java.util.ArrayList;

/**
 * Created by kibria on 23/10/17.
 */

public class NotificationAdapter extends ArrayAdapter {

    private static final String TAG="MyTag";

    Context context;
   public ArrayList<String> notificationText=new ArrayList<String>();

    public NotificationAdapter(Context context, ArrayList<String> notificationText){
        super(context, R.layout.notification_template,R.id.notificationText, notificationText);
        this.context=context;
        this.notificationText = notificationText;
    }


    class MyViewHolder{

        TextView notificationText;

        public MyViewHolder(View view){

            notificationText = (TextView) view.findViewById(R.id.notificationText);
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row=convertView;
        MyViewHolder myViewHolder=null;
        if(row==null){

            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.notification_template,parent,false);
            myViewHolder= new MyViewHolder(row);
            row.setTag(myViewHolder);
            Log.w(TAG,"creating new");
        }
        else {

            Log.i(TAG,"recycling stuff");
            myViewHolder= (MyViewHolder) row.getTag();
        }

        myViewHolder.notificationText.setText(notificationText.get(position));

        return row;
    }
}
