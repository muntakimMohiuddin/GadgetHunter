package com.example.mks.gadgethunter.shuvo.ui.adapters;

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

public class MessagesAdapter extends ArrayAdapter {

    private static final String TAG="MyTag";

    Context context;


    ArrayList<String> messages;
    ArrayList<String> name;

    public MessagesAdapter(Context context, ArrayList<String> name, ArrayList<String> messages){
        super(context, R.layout.messages_template,R.id.user_name, name);
        this.context=context;
        this.messages = messages;
        this.name = name;
    }


    class MyViewHolder{

        TextView userName;
        TextView lastMessages;

        public MyViewHolder(View view){

            userName = (TextView) view.findViewById(R.id.user_name);
            lastMessages = (TextView) view.findViewById(R.id.last_messages);
        }


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row=convertView;
        MyViewHolder myViewHolder=null;
        if(row==null){

            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.messages_template,parent,false);
            myViewHolder= new MyViewHolder(row);
            row.setTag(myViewHolder);
            Log.w(TAG,"creating new");
        }
        else {

            Log.i(TAG,"recycling stuff");
            myViewHolder= (MyViewHolder) row.getTag();
        }

        myViewHolder.userName.setText(name.get(position));
        myViewHolder.lastMessages.setText(messages.get(position));

        return row;
    }
}
