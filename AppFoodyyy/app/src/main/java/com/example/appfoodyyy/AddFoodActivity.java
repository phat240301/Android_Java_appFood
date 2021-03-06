package com.example.appfoodyyy;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class AddFoodActivity extends AppCompatActivity {
    EditText editTextNamefood,editTextPrice,editTextDescription;
    private Button buttonLuu, buttonBack;
    Spinner spinneruser;
    TextView textUsername;
    private User user;
    ImageButton ibtnCamera, ibtnFolder;
    ImageView imgNoImage;
    //
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        textUsername = (TextView) findViewById(R.id.textQuanAnOruser);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        textUsername.setText(""+ user.getId());
        textUsername.setVisibility(View.INVISIBLE);
        //anh xa button image
        ibtnCamera = (ImageButton) findViewById(R.id.editImageCapture);
        ibtnFolder =(ImageButton) findViewById(R.id.editImage);
        imgNoImage = (ImageView) findViewById(R.id.ImageFood);
        spinneruser = (Spinner) findViewById(R.id.spinnerUser);


        editTextNamefood = (EditText) findViewById(R.id.editTextName);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextDescription =(EditText) findViewById(R.id.editTextDescription);
        buttonLuu = (Button) findViewById(R.id.buttonSave);
        buttonBack = (Button) findViewById(R.id.buttonCancel);

        //S??? ki???n l??u M??n ??n
        buttonLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSave_onClick(view);
            }
        });
        //S??? Ki???n Tr??? v??? menu ch??nh
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ibtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, REQUEST_CODE_CAMERA );
                ActivityCompat.requestPermissions(
                        AddFoodActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA
                );
            }
        });

        ibtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
//                ActivityCompat.requestPermissions(
//                        AddFoodActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_CODE_FOLDER);
            }
        });
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
            imgNoImage.setImageBitmap(bitmap);
        }

        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgNoImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void buttonSave_onClick(View view) {
        DBHelper sqLiteOpenHelper = new DBHelper(getApplicationContext());
        Food food = new Food();
//        User user = (User) spinneruser.getSelectedItem();
//        food.setUsertId(user.getId());
        //Bitmap -> m???ng Byte.
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgNoImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] image = byteArray.toByteArray();


        food.setName(editTextNamefood.getText().toString());
        food.setPrice(Double.parseDouble(editTextPrice.getText().toString()));
        food.setDescription(editTextDescription.getText().toString());
//        Restaurant restaurant = (Restaurant) spinnerRestaurant.getSelectedItem();

        food.setImage(image);
        food.setUsertId(user.getId());
        if(sqLiteOpenHelper.createFood(food)){
            Intent intent = new Intent(AddFoodActivity.this, ListFoodActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(), "Th??m Th???t B???i", Toast.LENGTH_LONG).show();
        }
    }
}