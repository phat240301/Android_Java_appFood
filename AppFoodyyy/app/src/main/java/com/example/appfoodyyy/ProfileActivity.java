package com.example.appfoodyyy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {


    ImageView imageViewProfile;
    TextView textViewUsername;
    EditText editTextPassword, editTextPersonFullName,editTextTextAddress, editTextUsername;
    Button buttonChange, buttonBack;
    private  User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //anh xa
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        editTextPassword = (EditText) findViewById(R.id.txtPassword);
        editTextPersonFullName = (EditText) findViewById(R.id.txtfullname);
        editTextUsername = (EditText) findViewById(R.id.txtusername);
        editTextTextAddress = (EditText) findViewById(R.id.txtaddress);
        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfile);
        buttonChange = (Button) findViewById(R.id.buttonChange);
        buttonBack= (Button) findViewById(R.id.buttonBack);


        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        textViewUsername.setText(user.getName());

        showData(user.getId());

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "change image ?", Toast.LENGTH_SHORT).show();
            }
        });
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSave_onClick(view);
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ProfileActivity.this, HomeActivity.class);
                in.putExtra("user", user);
                startActivity(in);
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
    public void buttonSave_onClick(View view) {
        try{
            DBHelper data = new DBHelper(getApplicationContext());
            User curentaccount = data.find(user.getId());
//            String newUsername = textViewUsername.getText().toString();
            String newUsername = editTextUsername.getText().toString().trim();
            User temp = data.CheckUsernameProfile(newUsername);
            if(!newUsername.equalsIgnoreCase(curentaccount.getUsername()) && temp != null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Lỗi");
                builder.setMessage("Tên Tài khoản đã tồn tại");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                return;
            }
            curentaccount.setUsername(editTextUsername.getText().toString());
            curentaccount.setName(editTextPersonFullName.getText().toString());
            curentaccount.setAddress(editTextTextAddress.getText().toString());
            curentaccount.setImage(imageViewToByte(imageViewProfile));
            String password = editTextPassword.getText().toString();
            if(!password.isEmpty())
            {
                curentaccount.setPassword(editTextPassword.getText().toString());
            }if(data.update(curentaccount))
            {
                Intent in = new Intent(ProfileActivity.this, HomeActivity.class);
                in.putExtra("user", curentaccount);
                startActivity(in);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Lỗi");
                builder.setMessage("Thất bại");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }

        }catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Lỗi ");
            builder.setMessage(e.getMessage());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();

        }
    }

    private void showData(int id) {
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.ReadUser(id);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                editTextPassword.setText(cursor.getString(5));
                editTextTextAddress.setText(cursor.getString(3));
                editTextPersonFullName.setText(cursor.getString(2));
                editTextUsername.setText(cursor.getString(1));
                byte[] image = cursor.getBlob(6);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
                imageViewProfile.setImageBitmap(bitmap);;
            }
            cursor.close();
        }
    }
}