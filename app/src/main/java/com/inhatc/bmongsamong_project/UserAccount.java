package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserAccount {

    public UserAccount(){

    }

    private String idToken;
    private String userName;
    private String emailId;
    private String password;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}