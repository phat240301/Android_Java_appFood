package com.example.appfoodyyy;



public class Comment {
     private  int id;
     private String content;
     private int userId;
     private int postId;

    public Comment(String content, int postId, int userId) {
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
