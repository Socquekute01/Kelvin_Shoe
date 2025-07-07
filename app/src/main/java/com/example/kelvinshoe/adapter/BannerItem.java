package com.example.kelvinshoe.adapter;

// BannerItem.java
public class BannerItem {
    private String title;
    private String description;
    private String imageUrl;
    private int imageResource;
    private String actionUrl;

    public BannerItem(String title, String description, int imageResource, String actionUrl) {
        this.title = title;
        this.description = description;
        this.imageResource = imageResource;
        this.actionUrl = actionUrl;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getImageResource() { return imageResource; }
    public String getActionUrl() { return actionUrl; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
}
