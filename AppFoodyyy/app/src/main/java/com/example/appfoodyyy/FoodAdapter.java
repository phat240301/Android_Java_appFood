package com.example.appfoodyyy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FoodAdapter extends ArrayAdapter<Food> {

    private ListFoodActivity context;
    private int layout;
    private List<Food> foods;
    private int role;
//    private  Restaurant restaurant;

    public FoodAdapter(@NonNull ListFoodActivity context, int resource, @NonNull List<Food> objects, int role) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.foods = objects;
        this.role = role;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Food food = foods.get(position);
        View view = LayoutInflater.from(context).inflate(layout, null);
        TextView textViewName = view.findViewById(R.id.tvNameFood);
        textViewName.setText(food.getName());
        TextView textViewPrice = view.findViewById(R.id.tvPriceFood);
        textViewPrice.setText(String.valueOf(food.getPrice()));
        TextView textViewDescripton = view.findViewById(R.id.tvDescriptonFood);
        textViewDescripton.setText(food.getDescription());
        TextView textViewRestaurant = view.findViewById(R.id.tvRestaurant);
        ListView listViewFood = view.findViewById(R.id.listviewFood);

        DBHelper dbHelper = new DBHelper(context);
        textViewRestaurant.setText(dbHelper.getIdRes(food.getUsertId()));

        User user = new User();

        ImageView imageFood =  view.findViewById(R.id.updateImages);
        ImageView imgdelete = view.findViewById(R.id.Imgdelete);
        ImageView imgedit = view.findViewById(R.id.Imgedit);
        if(role == 1)
        {
            imgdelete.setVisibility(View.GONE);
            imgedit.setVisibility(View.GONE);
        }
        imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Xoa", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView imagefood = (ImageView) view.findViewById(R.id.imageIdHinh);
        byte[] image = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        imagefood.setImageBitmap(bitmap);
        context.getFoodFromAdapter(food.getId());
        imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Sua", Toast.LENGTH_SHORT).show();
                context.Dialogupdate(food.getName(), food.getPrice(), food.getDescription() ,food.getImage(), food.getUsertId(), food.getId());
            }
        });

        imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {context.DialogDelete(food.getName(), food.getId());

            }
        });

        return view;
    }
}
