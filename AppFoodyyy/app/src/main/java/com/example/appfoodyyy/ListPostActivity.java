package com.example.appfoodyyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ListPostActivity extends AppCompatActivity {
    private ListView listViewPost;
    BottomNavigationView bottomNavigationView;
    ImageView imageViewComment;
    EditText editTextComment;
    private Food food;
    private User user;
    private int id_Food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_post);
        initView();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        int idFood = (int) intent.getSerializableExtra("idFood");
        id_Food = idFood;
        loadDataWithIdFood();
        Toast.makeText(getApplicationContext(),String.valueOf(user.getId()), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"food: " +idFood, Toast.LENGTH_LONG).show();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        Intent i1n = new  Intent (getApplicationContext(), HomeActivity.class);
                        i1n.putExtra("user", user);
                        startActivity(i1n);
                        overridePendingTransition(0,0);
                        return true;
//                    case  R.id.addOption:
////                        startActivity(new Intent (getApplicationContext(), ThemThucAnActivity.class));
//                        Intent in = new  Intent (getApplicationContext(), AddFoodActivity.class);
//                        in.putExtra("user", user);
//                        startActivity(in);
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.listfood:
                        startActivity(new Intent(getApplicationContext(), ListFoodActivity.class));
                        Intent intent = new Intent(getApplicationContext(), ListFoodActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.accountprofile:
//                        Intent intent1 = new Intent(getApplicationContext(), ListPostActivity.class);
//                        intent1.putExtra("user", user);
//                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.detailpost:
                        Intent intentDetailPost = new Intent(getApplicationContext(), DetailPostActivity.class);
//                        intent1.putExtra("user", user);
                        startActivity(intentDetailPost);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        editTextComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("idFood", idFood);
                startActivity(intent);
            }
        });
    }

    private void loadData(){
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        List<Post> posts = dbHelper.findAllPost();
        if(!posts.isEmpty()){
            listViewPost.setAdapter(new PostAdapter(ListPostActivity.this, R.layout.line_post, posts));
        }
    }

    private void loadDataWithIdFood(){
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        List<Post> posts = dbHelper.findAllPostByIdFood(id_Food);
        if(!posts.isEmpty()){
            listViewPost.setAdapter(new PostAdapter(ListPostActivity.this, R.layout.line_post, posts));
        }
    }

    private void initView(){
        listViewPost = findViewById(R.id.listviewpost);
        imageViewComment = findViewById(R.id.btnCommentPostDetail);
        editTextComment = findViewById(R.id.editTextComment);
    }
}