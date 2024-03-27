package com.example.ChatApp.dto;

import org.bson.types.ObjectId;

import java.util.Date;

public class UserUpdateDto {
    public String Id;
    public String DisplayName;
    public  String Email;
    public String Tag;
    public Date Birth;

    public Date getBirth() {
        return Birth;
    }

    public void setBirth(Date birth) {
        Birth = birth;
    }
    public ObjectId getId() {
        return null;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

}
