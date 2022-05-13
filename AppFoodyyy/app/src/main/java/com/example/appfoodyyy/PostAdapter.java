package com.example.appfoodyyy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post>{

    private ListPostActivity context;
    private int layout;
    private List<Post> posts;

    private RecyclerView recyclerViewComment;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    CommentAdapter commentAdapter;
    private ArrayList<Comment> modelArrayList;

    public PostAdapter(@NonNull ListPostActivity context, int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.posts = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post post = posts.get(position);
        View view = LayoutInflater.from(context).inflate(layout, null);
        TextView textViewContent = view.findViewById(R.id.tvPostDetailContent);
        textViewContent.setText(String.valueOf(post.getContent()));
        TextView textViewUser = view.findViewById(R.id.tvUser);
        TextView textViewFood = view.findViewById(R.id.tvNameFood);
        TextView textViewRestaurant = view.findViewById(R.id.tvPostRestaurant);
        TextView textViewLike = view.findViewById(R.id.tvLike);
        TextView textViewComment = view.findViewById(R.id.tvComment);
        TextView textViewScore = view.findViewById(R.id.tvRate);

        ImageView imageViewLike = view.findViewById(R.id.btnLike);
        ImageView imageViewComment = view.findViewById(R.id.btnComment);

        dbHelper = new DBHelper(context);
        textViewFood.setText(dbHelper.getIdFood(post.getFoodId()));
        textViewUser.setText(dbHelper.getIdUser(post.getUserId()));
        textViewRestaurant.setText(dbHelper.getIdRes(post.getFoodId()));
        textViewScore.setText(String.valueOf(dbHelper.getIdScore(post.getId())));

        textViewLike.setText(String.valueOf(dbHelper.countIdUser(post.getId())) +" like");
        textViewComment.setText(String.valueOf(dbHelper.countComment(post.getId())) +" comment");

        final Like like = new Like();
        like.setPostId(post.getId());
        like.setUserId(post.getUserId());

        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbHelper.findUserIdAndPostId(post.getUserId(), post.getId()) == null) {
                    if (dbHelper.createLike(like)) {
                        Toast.makeText(context, "successful", Toast.LENGTH_LONG).show();
                        textViewLike.setText(String.valueOf(dbHelper.countIdUser(post.getId())) + " like");
                        imageViewLike.setImageResource(R.drawable.ic_favorite);
                    } else {
                        Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    dbHelper.getReadableDatabase().execSQL("delete from likes where userId = '" + post.getUserId() + "' and postId = '" + post.getId() + "'");
                    Toast.makeText(context, "deleted", Toast.LENGTH_LONG).show();
                    textViewLike.setText(String.valueOf(dbHelper.countIdUser(post.getId())) + " like");
                    imageViewLike.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        });

        imageViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), DetailPostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",post.getId());
                bundle.putInt("user", post.getUserId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }

}
