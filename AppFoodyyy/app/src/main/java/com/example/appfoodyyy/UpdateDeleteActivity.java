package com.example.appfoodyyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class UpdateDeleteActivity extends AppCompatActivity {
    EditText editTextNamefood,editTextPrice,editTextDescription;
    private Button buttonLuu, buttonBack;
    TextView textUsername;
    private User user;
    private  Food food;
    ImageView imgNoImage;
    byte [] image;
    ImageButton ibtnCamera, ibtnFolder;
    DBHelper data ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        ibtnCamera = (ImageButton) findViewById(R.id.imageCapture);
        ibtnFolder = (ImageButton) findViewById(R.id.imageFolder);
        imgNoImage = (ImageView) findViewById(R.id.updateImages);

        editTextNamefood = (EditText) findViewById(R.id.edtFoodName);
        editTextPrice = (EditText) findViewById(R.id.editFoodPrice);
        editTextDescription =(EditText) findViewById(R.id.edtFoodDescription);
        buttonLuu = (Button) findViewById(R.id.btnEditFood);
        buttonBack = (Button) findViewById(R.id.btnHuyFood);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("food");

        editTextNamefood.setText(food.getName());
        editTextPrice.setText(""+food.getPrice());
        editTextDescription.setText(food.getDescription());
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        imgNoImage.setImageBitmap(bitmap);
         data = new DBHelper(this);
    }

    private byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArray);
        byte[] image = byteArray.toByteArray();
        return image;
    }
}