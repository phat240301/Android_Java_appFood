package com.example.appfoodyyy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailPostActivity extends AppCompatActivity {

    private RecyclerView recyclerViewComment;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    CommentAdapter commentAdapter;
    private ArrayList<Comment> modelArrayList;
    EditText comment;
    Button send;
    ImageView Like,Rate;
    private Post post;
    TextView textViewContent, textViewUser, textViewFood, textViewRestaurant, textViewCountLike, textViewCountComment, textViewScore;
    RatingBar ratingBarRate;
    Dialog dialog;
    public static DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        int idPro = bundle.getInt("id",0);
        int idUser = bundle.getInt("user",0);

        dbHelper = new DBHelper(DetailPostActivity.this);
        initView();
        loadData(idPro,idUser);
        showAllPostByIDpost(idPro);
        insertData(idPro, idUser);


        Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like like = new Like();
                like.setPostId(idPro);
                like.setUserId(idUser);
                if (dbHelper.findUserIdAndPostId(idUser,idPro) == null) {
                    if (dbHelper.createLike(like)) {
                        Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                        textViewCountLike.setText(String.valueOf(dbHelper.countIdUser(idPro)) +" like");
                    } else {
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    dbHelper.getReadableDatabase().execSQL("delete from likes where userId = '" + idUser + "' and postId = '" + idPro + "'");
                    Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_LONG).show();
                    textViewCountLike.setText(String.valueOf(dbHelper.countIdUser(idPro)) +" like");
                }
            }
        });

        Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void showAllPostByIDpost(int idPro) {
        Cursor cursor = dbHelper.ReadPostByIDpost(idPro);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                textViewContent.setText(cursor.getString(1));
                textViewFood.setText(dbHelper.getIdFood(cursor.getInt(2)));
                textViewUser.setText(dbHelper.getIdUser(cursor.getInt(3)));
                textViewRestaurant.setText(dbHelper.getIdRes(cursor.getInt(2)));
                textViewScore.setText(String.valueOf(dbHelper.getIdScore(idPro)));
            }
            cursor.close();
        }
    }

    private void loadData(int idPro, int idUser){
        modelArrayList = new ArrayList<>();
        modelArrayList = dbHelper.displayData(idPro);
        commentAdapter=new CommentAdapter(modelArrayList, getApplicationContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerViewComment.setLayoutManager(linearLayoutManager);
        recyclerViewComment.setAdapter(commentAdapter);

        textViewCountLike.setText((dbHelper.countIdUser(idPro)) +" like");
        textViewCountComment.setText((dbHelper.countComment(idPro)) +" comment");
    }

    private void cleardata() {
        comment.setText("");
    }

    public void insertData(int idPost,int idUser) {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues cv=new ContentValues();
                cv.put("content",comment.getText().toString());
                cv.put("postId",idPost);
                cv.put("userId",idUser);

                Long recid = sqLiteDatabase.insert("comment",null,cv);
                if (recid!=null){
                    Toast.makeText(getApplicationContext(), "successfully insert", Toast.LENGTH_SHORT).show();
                    cleardata();
                    loadData(idPost,idUser);
                }else{
                    Toast.makeText(getApplicationContext(), "something wrong try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView(){
        comment = (EditText) findViewById(R.id.editTextComment);
        send = (Button) findViewById(R.id.btnComment);
        recyclerViewComment = (RecyclerView) findViewById(R.id.rev_Comment);
        textViewContent = findViewById(R.id.tvPostDetailContent);
        textViewFood = findViewById(R.id.tvNameFoodPostDetail);
        textViewUser = findViewById(R.id.tvUserPostDetail);
        textViewRestaurant = findViewById(R.id.tvRestaurantPostDetail);
        textViewCountLike = findViewById(R.id.textViewLike);
        textViewCountComment = findViewById(R.id.textViewComment);
        Like = (ImageView) findViewById(R.id.btnLikePostDetail);
        Rate = (ImageView) findViewById(R.id.btnRatingPostDetail);
        textViewScore = findViewById(R.id.textViewRate);
    }
}