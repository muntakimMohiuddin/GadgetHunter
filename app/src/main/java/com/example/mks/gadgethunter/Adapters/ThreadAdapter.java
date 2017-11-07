package com.example.mks.gadgethunter.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mks.gadgethunter.Activities.MainTabs;
import com.example.mks.gadgethunter.Items.ThreadItems;
import com.example.mks.gadgethunter.Models.Post;
import com.example.mks.gadgethunter.R;

import java.util.List;


/**
 * Created by HP on 09-Oct-17.
 */

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    public List<Post> list;
    public Context context;

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.threads_cards, parent, false);
        return new ViewHolder(v);
    }

    public ThreadAdapter(List<Post> list, Context context) {
        super();
        this.list = list;
        this.context = context;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Post item = list.get(position);
        holder.username.setText(item.getUsername());
        if (item.getTitle().length()>15)
        holder.threadTitle.setText(item.getTitle().substring(0, 15)+"...");
        else
            holder.threadTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, threadTitle;
        TextView info;
        RelativeLayout card;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            threadTitle = (TextView) itemView.findViewById(R.id.thread_title);
            card = (RelativeLayout) itemView.findViewById(R.id.cards);
        }
    }
}
