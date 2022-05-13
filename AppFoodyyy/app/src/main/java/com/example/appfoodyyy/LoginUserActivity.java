package com.example.appfoodyyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginUserActivity extends AppCompatActivity {
    EditText edtUserusername , edtUserpassword;
    Button btnLoginUser;
    TextView textViewSignUp;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        //Ánh Xạ
        edtUserusername = findViewById(R.id.loginUser_username);
        edtUserpassword = findViewById(R.id.loginUser_password);
        btnLoginUser = findViewById(R.id.loginUser_btn);
        textViewSignUp = findViewById(R.id.textView_haveaccount);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUserActivity.this, SignUpUserActivity.class));
            }
        });

        DBHelper data = new DBHelper(this);
        //Sự Kiện Đăng Nhập
        btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edtUserusername.getText().toString();
                String PassWord = edtUserpassword.getText().toString();

                //Kiểm tra dữ nhập liệu
                if(userName.isEmpty() || PassWord.isEmpty()){
                    Toast.makeText(LoginUserActivity.this, "Không được bỏ trống ô này !", Toast.LENGTH_SHORT).show();
                }else{
//                    Boolean checkUserPass = data.checkUserNamePassword(userName, PassWord);
                    user = data.loginUser(userName, PassWord);
                    if(user != null){
                        Toast.makeText(LoginUserActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginUserActivity.this, "Đăng nhập không thành công !", Toast.LENGTH_SHORT).show();
                    }
                }//End check duplicate user pass

            }
        });


    }
}