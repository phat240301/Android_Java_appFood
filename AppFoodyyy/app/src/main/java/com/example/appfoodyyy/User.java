package com.example.appfoodyyy;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String username;
    private String address;
    private int roles;
    private String password;
    private byte[] image;

    public  User()
    {}

    public User(int id, String name, String username, String address, int roles, String password, byte[] image) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.address = address;
        this.roles = roles;
        this.password = password;
        this.image = image;
    }

    public int getRoles() {
        return roles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
