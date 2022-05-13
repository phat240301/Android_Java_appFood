package com.example.appfoodyyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UpdateFoodActivity extends AppCompatActivity {

    TextView textRestaurant;
    EditText editnamfood, editprice, editDescription;
    Button btnUpdate , btnCancel, btnDelete;
    private  Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        //ánh xạ
        editnamfood = (EditText) findViewById(R.id.edttenmonan);
        editprice = (EditText) findViewById(R.id.edtgiamonan);
        editDescription = (EditText) findViewById(R.id.edtMota);
        btnUpdate = (Button) findViewById(R.id.btnSuaMonAn);
        btnDelete = (Button) findViewById(R.id.btndelete);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        textRestaurant = (TextView) findViewById(R.id.idcuanguoidung);

        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("food");
        editnamfood.setText(food.getName());
        editprice.setText(""+food.getPrice());
        editDescription.setText(food.getDescription());



    }
}