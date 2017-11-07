package com.example.mks.gadgethunter.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mks.gadgethunter.Activities.CommentActivity;
import com.example.mks.gadgethunter.Activities.PostView;
import com.example.mks.gadgethunter.Models.Post;
import com.example.mks.gadgethunter.R;

import java.util.List;

/**
 * Created by HP on 10-Oct-17.
 */

public class FeaturedPostAdapter extends RecyclerView.Adapter<FeaturedPostAdapter.ViewHolder> {

    public List<Post> list;
    public Context context;


    public FeaturedPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_cards, parent, false);
        return new FeaturedPostAdapter.ViewHolder(v);
    }

    public FeaturedPostAdapter(List<Post> list, Context context) {
        super();
        this.list = list;
        this.context = context;

    }

    @Override
    public void onBindViewHolder(FeaturedPostAdapter.ViewHolder holder, final int position) {
        final Post item = list.get(position);
        holder.username.setText(item.getUsername());
        holder.postTitle.setText(item.getTitle());
        holder.noOfComments.setText("Comments: " + item.numberOfComments);
        if (item.imageUrl != null && item.imageUrl.length() > 0)
            Glide.with(context).load(item.getImageUrl()).into(holder.postImage);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you        clicked         " + item.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, PostView.class);
                intent.putExtra("postId", item.postId);
                intent.putExtra("postContent", item.content);
                intent.putExtra("postUid", item.uid);
                intent.putExtra("postImageUrl", item.imageUrl);
                intent.putExtra("Admin Post", "true");
                intent.putExtra("postTitle",item.getTitle());
                context.startActivity(intent);
            }
        });
        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", item.postId);
                intent.putExtra("postUid", item.uid);
                intent.putExtra("Admin Post", "true");
                intent.putExtra("postTitle",item.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, postTitle;
        TextView info;
        RelativeLayout card;
        ImageView postImage;
        TextView noOfComments;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            postTitle = (TextView) itemView.findViewById(R.id.title);
            card = (RelativeLayout) itemView.findViewById(R.id.cards);
            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            noOfComments = (TextView) itemView.findViewById(R.id.featured_comments);
        }
    }
}

