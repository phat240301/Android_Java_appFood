package com.example.appfoodyyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ListFoodActivity extends AppCompatActivity {
    private ListView listViewFood;
    private User user;
    ArrayList<Food> arrayFood;
    FoodAdapter adapter;
    ImageView imgedit, imgdelete;
    DBHelper dbHelper;
    TextView textrole;
    private Food food;
    ImageButton ibtnCamera,ibtnFolder,ibtnEdit,ibtnDelete;
    ImageView imgNoImage,imgEdit,imgDelete;
    Dialog dialog;
    EditText edtTen,edtGia,edtMota,edtQuan;
    Button btnupdate,btnHuy;

    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        listViewFood = findViewById(R.id.listviewFood);
        dbHelper = new DBHelper(ListFoodActivity.this);

        dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua);
        textrole = (TextView) findViewById(R.id.textRole);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        textrole.setText(""+user.getRoles());

        ibtnCamera = (ImageButton) dialog.findViewById(R.id.imageCapture);
        ibtnFolder = (ImageButton) dialog.findViewById(R.id.imageFolder);
        imgNoImage = (ImageView) dialog.findViewById(R.id.updateImages);

        edtTen = (EditText) dialog.findViewById(R.id.edtFoodName);
        edtGia = (EditText) dialog.findViewById(R.id.editFoodPrice);
        edtMota = (EditText) dialog.findViewById(R.id.edtFoodDescription);
        //edtQuan = (EditText) dialog.findViewById(R.id.edtRestaurant);

         btnupdate = (Button) dialog.findViewById(R.id.btnEditFood);
         btnHuy = (Button) dialog.findViewById(R.id.btnHuyFood);
        //image


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
//                        startActivity(new Intent(getApplicationContext(), ListFoodActivity.class));
//                        Intent intent = new Intent(getApplicationContext(), ListFoodActivity.class);
//                        intent.putExtra("restaurant", restaurant);
//                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.accountprofile:
                        Intent intentpost = new  Intent (getApplicationContext(), ListPostActivity.class);
                        intentpost.putExtra("user", user);
                        startActivity(intentpost);
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

    LoadData();
    }

    public void getFoodFromAdapter(int idfood){
        listViewFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = ((TextView) view.findViewById(R.id.tvRestaurant)).getText().toString();
//                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
//                toast.show();
                int temp = dbHelper.returnIdFood(selected);
                Toast toast1= Toast.makeText(getApplicationContext(), "select : "+temp, Toast.LENGTH_LONG);
                toast1.show();
                Intent intentpost = new  Intent (getApplicationContext(), ListPostActivity.class);
                intentpost.putExtra("user", user);
                intentpost.putExtra("idFood", temp);
                startActivity(intentpost);
            }
        });
    }

    private void LoadData()
    {
        DBHelper data = new DBHelper(getApplicationContext());
        List<Food> foods = data.findAllFood();
        if(!foods.isEmpty()){
            listViewFood.setAdapter(new FoodAdapter(ListFoodActivity.this, R.layout.thuc_an_layout, foods,user.getRoles()));
        }
    }

    private byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] image = byteArray.toByteArray();
        return image;
    }

    public void Dialogupdate (String foodname, double foodprice, String Mota, byte[] image, int idUser,final int id)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        imgNoImage.setImageBitmap(bitmap);

        edtTen.setText(foodname);
        edtGia.setText(""+foodprice);
        edtMota.setText(Mota);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtTen.getText().toString().trim();
                String price = edtGia.getText().toString().trim();
                String decrip = edtMota.getText().toString().trim();

                Bitmap bitmap = ((BitmapDrawable) imgNoImage.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                byte[] image = byteArray.toByteArray();
                if (name.equals("") || price.equals("") || decrip.equals("") ){
                    Toast.makeText(ListFoodActivity.this, "Please Fill adl fields !", Toast.LENGTH_SHORT).show();
                }else {
                    dbHelper.UPDATE_FOODS(edtTen.getText().toString().trim()
                            ,edtGia.getText().toString().trim()
                            ,edtMota.getText().toString().trim()
                            ,image ,idUser, id);
                    Toast.makeText(ListFoodActivity.this, "Edit successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    LoadData();
                }
            }
            });
        dialog.show();

        ibtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, REQUEST_CODE_CAMERA );
                ActivityCompat.requestPermissions(
                        ListFoodActivity.this,
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
            }
        });

    }

    public void DialogDelete(final String name, final int id){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setMessage("Do you want to delete "+ name +" ?");
        dialogDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.QueryData("DELETE FROM food WHERE id = '" + id + "'");
                Toast.makeText(ListFoodActivity.this, name + " is deleted !",Toast.LENGTH_SHORT).show();
                LoadData();
            }
        });
        dialogDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogDelete.show();
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
}