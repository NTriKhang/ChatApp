package com.example.ChatApp.dto;

import org.bson.types.ObjectId;

import java.util.Date;

public class UserUpdateDto {
    public String Id;
    public String DisplayName;
    public  String Email;
    public String Tag;
    public Date Birth;
    public String ImagePath;
    public Date Edited_day;

    public Date getCreated_day() {
        return Created_day;
    }

    public void setCreated_day(Date created_day) {
        Created_day = created_day;
    }

    public Date Created_day;
    public String BackgroundImagePath;

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

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public Date getEdited_day() {
        return Edited_day;
    }

    public void setEdited_day(Date edited_day) {
        Edited_day = edited_day;
    }

    public String getBackgroundImagePath() {
        return BackgroundImagePath;
    }

    public void setBackgroundImagePath(String backgroundImagePath) {
        BackgroundImagePath = backgroundImagePath;
    }
}
