package com.example.appfoodyyy;

import java.io.Serializable;

public class Food implements Serializable {

    private int id;
    private String name;
    private double price;
    private String description;
    private byte[] Image;
    //private int restaurantId;
    private int usertId;
    public Food(){}

    public Food(int id, String name, double price, String description, byte[] image, int usertId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        Image = image;
        this.usertId = usertId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUsertId() {
        return usertId;
    }

    public void setUsertId(int usertId) {
        this.usertId = usertId;
    }


}
