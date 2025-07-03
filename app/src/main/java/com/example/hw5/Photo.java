package com.example.hw5;

public class Photo {
    private int id;
    private String title;
    private String description;
    private String imagePath;

    public Photo(int id, String title, String description, String imagePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
}


