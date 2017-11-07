package com.example.mks.gadgethunter.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mks.gadgethunter.Models.Comments;
import com.example.mks.gadgethunter.R;

import java.util.List;

/**
 * Created by HP on 25-Oct-17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public List<Comments> list;
    public Context context;


    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_cards, parent, false);
        return new CommentAdapter.ViewHolder(v);
    }


    public CommentAdapter(List<Comments> list, Context context) {
        super();
        this.list = list;
        this.context = context;

    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, final int position) {
        final Comments item = list.get(position);
        holder.commenterName.setText(item.commenterName);
        if (item.content.length() > 15) {
            holder.commentContent.setText(item.content.substring(0, 15));
            holder.ViewFullComment.setVisibility(View.VISIBLE);
        } else
            holder.commentContent.setText(item.content);
        holder.ViewFullComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.commentContent.setText(item.content);
                v.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commenterName, commentContent, ViewFullComment;
        RelativeLayout commentCard;

        public ViewHolder(View itemView) {
            super(itemView);
            commenterName = (TextView) itemView.findViewById(R.id.commenter_name);
            commentContent = (TextView) itemView.findViewById(R.id.content);
            commentCard = (RelativeLayout) itemView.findViewById(R.id.cards);
            ViewFullComment = (TextView) itemView.findViewById(R.id.view_full_comment);

        }
    }
}

