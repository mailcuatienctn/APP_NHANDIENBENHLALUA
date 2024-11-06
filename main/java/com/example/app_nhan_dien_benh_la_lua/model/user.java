package com.example.app_nhan_dien_benh_la_lua.model;

import com.google.firebase.Timestamp;

public class user {
    private String user;

    private String phone;
    private String img;
    private Timestamp createdTime;

    public user() {
    }

    public user(String user, String phone, String img, Timestamp createdTime) {
        this.user = user;
        this.phone = phone;
        this.img = img;
        this.createdTime = createdTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "user{" +
                "user='" + user + '\'' +
                ", phone='" + phone + '\'' +
                ", img='" + img + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}
