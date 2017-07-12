package com.htnguyen.healthy.model;

public class User {

    private String userId;
    private String userName;
    private String userEmail;
    private String image;
    private int gender;
    private int height;
    private int weight;
    private int heart;
    private int online;
    private String userFriend;

    public User() {
    }


    public User(String userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public User(String userId, String userName, String userEmail, int gender, int height, int weight, int heart, String userFriend) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.heart = heart;
        this.userFriend = userFriend;
    }

    public User(String userName, int gender, int height, int weight, int heart, String image) {
        this.userName = userName;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.heart = heart;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public String getUserFriend() {
        return userFriend;
    }

    public void setUserFriend(String userFriend) {
        this.userFriend = userFriend;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
