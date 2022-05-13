package com.example.appfoodyyy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class SignUpUserActivity extends AppCompatActivity {
    EditText edtUserusername, edtUserpassword, edtUserrepassword, edtUserfullname, edtUseraddress;
    Button buttonDangKyUser;
    ImageView imageUser, imageChossen, imageCapture;
    TextView textLogin;
    Spinner spinnerUser;
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 654;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        edtUserusername = (EditText) findViewById(R.id.registerUser_username);
        edtUserfullname = (EditText) findViewById(R.id.registerUser_fullname);
        edtUseraddress = (EditText) findViewById(R.id.registerUser_address);
        edtUserpassword = (EditText) findViewById(R.id.registerUser_password);
        edtUserrepassword = (EditText) findViewById(R.id.registerUser_repassword);
        imageUser = (ImageView) findViewById(R.id.imageViewSignUp);
        imageChossen = (ImageView) findViewById(R.id.imageViewChossenFile);
        imageCapture = (ImageView) findViewById(R.id.imageViewCapture);

        spinnerUser = (Spinner) findViewById(R.id.spinnerUser);
        loadDataSpinner();
        textLogin = (TextView) findViewById(R.id.tv_move_login);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(SignUpUserActivity.this,LoginUserActivity.class);
               startActivity(intent);
            }
        });
        DBHelper data = new DBHelper(this);
        //button
        buttonDangKyUser = (Button) findViewById(R.id.registerUser_btn);

        imageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, REQUEST_CODE_CAMERA );
                ActivityCompat.requestPermissions(
                        SignUpUserActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA
                );
            }
        });

        imageChossen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
//                ActivityCompat.requestPermissions(
//                        SignUpActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_CODE_FOLDER
//                );
            }
        });



        //sự kiện đăng ký user
        buttonDangKyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullName = edtUserfullname.getText().toString();
                String userName = edtUserusername.getText().toString();
                String Address = edtUseraddress.getText().toString();
                String PassWord = edtUserpassword.getText().toString();
                String RePassWord = edtUserrepassword.getText().toString();
                byte[] imageUsers = imageViewToByte(imageUser);
                //int PhanQuyen = 2;
                User user = new User();
                Role role = (Role) spinnerUser.getSelectedItem();
                int PhanQuyen = role.getId();
//                if(userName.equals(""))
                //Kiểm tra dữ liệu nhập vào ko được để trống
                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(Address) || TextUtils.isEmpty(PassWord) ||TextUtils.isEmpty(RePassWord)){
                    Toast.makeText(SignUpUserActivity.this, "Không được bỏ trống ô này ", Toast.LENGTH_SHORT).show();
                }else{

                    //Kiểm tra có trùng mật khẩu không
                    if(PassWord.equals(RePassWord)){
                        Boolean checkName = data.checkUserNameUser(userName);
//                        String username, String fullname, String address, int role, String password, byte[] image
                        if(checkName == false){
                            Boolean insertData = data.insertDataUser(userName, fullName, Address, PhanQuyen, PassWord, imageUsers);

                            if(insertData == true){
                                Toast.makeText(SignUpUserActivity.this, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginUserActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpUserActivity.this, "Đăng ký không thành công !", Toast.LENGTH_SHORT).show();
                            }//End insertdata to database

                        }else{
                            Toast.makeText(SignUpUserActivity.this, "Đã tồn tại tên người dùng trong DB !", Toast.LENGTH_SHORT).show();
                        }//End check Username exists

                    }else{
                        Toast.makeText(SignUpUserActivity.this, "Mật khẩu bị trùng !", Toast.LENGTH_SHORT).show();
                    }//End check Duplicate password

                } //end check empty fields
            }
        });
    }
    private byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] image = byteArray.toByteArray();
        return image;
    }



    private void loadDataSpinner(){
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        List<Role> roles = dbHelper.findAllRole();
        if(!roles.isEmpty()){
            spinnerUser.setAdapter(new RoleAdapter(getApplicationContext(), R.layout.user_layout, roles));
        }
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.Role_id, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerUser.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }else {
                    Toast.makeText(this, "You don't permit to open camera !!!", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_FOLDER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("/image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                }else {
                    Toast.makeText(this, "You don't permit to open library images", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageUser.setImageBitmap(bitmap);
        }

        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageUser.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}