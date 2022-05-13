package com.example.appfoodyyy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RoleAdapter extends ArrayAdapter<Role> {

    private Context context;
    private int layout;
    private List<Role> roles;

    public RoleAdapter(@NonNull Context context, int resource, @NonNull List<Role> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.roles = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Role role = roles.get(position);
        View view = LayoutInflater.from(context).inflate(layout, null);
        TextView textViewUserName = view.findViewById(R.id.textViewUserName);
        textViewUserName.setText(role.getName());
        return view;
    }
}
