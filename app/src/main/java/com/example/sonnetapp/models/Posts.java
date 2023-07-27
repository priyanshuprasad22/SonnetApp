package com.example.sonnetapp.models;

public class Posts {

    String title,description,image,timestamp,name,userimage;

    public Posts()
    {

    }


    public Posts(String title, String description, String image, String timestamp, String name, String userimage) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.timestamp = timestamp;
        this.name = name;
        this.userimage = userimage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }
}
