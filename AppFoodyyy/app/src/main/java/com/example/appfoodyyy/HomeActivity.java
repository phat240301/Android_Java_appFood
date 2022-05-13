package com.example.appfoodyyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    TextView textnameUser;
    private User user;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textnameUser = findViewById(R.id.textViewUser);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        textnameUser.setText(""+ user.getRoles());


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.addOption:
//                        startActivity(new Intent (getApplicationContext(), ThemThucAnActivity.class));
                        Intent in = new  Intent (getApplicationContext(), AddFoodActivity.class);
                        in.putExtra("user", user);
                        startActivity(in);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.listfood:
//                        startActivity(new Intent(getApplicationContext(), ListFoodActivity.class));
                        Intent intent = new Intent(getApplicationContext(), ListFoodActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.accountprofile:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent1.putExtra("user", user);
                        startActivity(intent1);
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
    }
}