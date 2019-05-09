package com.a161328.demo3.Model;

import java.util.ArrayList;

public class User {

    private String userName, userEmail, userLevel;
    private ArrayList<Record> userRec;
    private int userPoint;

    private static User ourInstance = new User();

    public static User getInstance() {
        if (ourInstance == null) {
            ourInstance = new User();
        }
        return ourInstance;
    }

    private User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Record> getUserRec() {
        return userRec;
    }

    public void setUserRec(ArrayList<Record> userRec) {
        this.userRec = userRec;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
}
