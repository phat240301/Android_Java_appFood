package com.example.appfoodyyy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddPostActivity extends AppCompatActivity {
    Button btnDang, btnHuy, btnSubmit, btnView;
    ImageView imageViewBack, imageViewScore;
    EditText editTextEditContent;
    Dialog dialog;
    RatingBar ratingBar;
    TextView textViewScore,textViewRate;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addpost);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        int idFood = (int) intent.getSerializableExtra("idFood");
        Toast.makeText(getApplicationContext(),String.valueOf(user.getId()), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"food: " +idFood, Toast.LENGTH_LONG).show();

        dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rating);

        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        btnView = (Button) dialog.findViewById(R.id.btnViewRate);
        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        textViewRate = (TextView) dialog.findViewById(R.id.TextViewRate);

        initView();
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEditContent.setText("");
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListPostActivity.class));
            }
        });

        btnDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper sqLiteOpenHelper = new DBHelper(getApplicationContext());
                Post post = new Post();
                post.setContent(editTextEditContent.getText().toString());
                post.setUserId(user.getId());
                post.setFoodId(idFood);
                post.setScore(Double.parseDouble(textViewScore.getText().toString()));
                if (sqLiteOpenHelper.createPost(post)) {
                    Intent intent = new Intent(AddPostActivity.this, ListPostActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        imageViewScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        textViewRate.setText(""+ratingBar.getRating());
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textViewScore.setText(textViewRate.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    public void initView() {
        btnDang = (Button) findViewById(R.id.btnPost);
        btnHuy = (Button) findViewById(R.id.btnCancel);
        imageViewBack = (ImageView) findViewById(R.id.imageback);
        imageViewScore = (ImageView) findViewById(R.id.imageScore);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        editTextEditContent = (EditText) findViewById(R.id.editTextContentPost);
    }
}
