package com.example.appfoodyyy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> commentArrayList;
    private Context context;

    public CommentAdapter(ArrayList<Comment> commentArrayList, Context context) {
        this.commentArrayList = commentArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentArrayList.get(position);
        holder.txtItemComment.setText(comment.getContent());
        DBHelper dbHelper = new DBHelper(context);
        holder.txtItemUser.setText(dbHelper.getIdUser(comment.getUserId()));
////        Random random=new Random();
////        int color= Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(256));
////        holder.icon.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtItemComment,txtItemUser;
        private ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemComment = (TextView) itemView.findViewById(R.id.tvNameUser);
            txtItemUser = (TextView) itemView.findViewById(R.id.tvCommentpost);
            //icon=(ImageView)itemView.findViewById(R.id.icon);
        }
    }
}