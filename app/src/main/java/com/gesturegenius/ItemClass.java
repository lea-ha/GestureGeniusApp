package com.gesturegenius;

public class ItemClass {
    private String english_name, arabic_name;
    private String imageURL;

    public ItemClass(String english_name, String arabic_name, String imageURL) {
        this.english_name = english_name;
        this.arabic_name = arabic_name;
        this.imageURL = imageURL;
    }

    public String getEnglishName() {
        return english_name;
    }

    public String getArabicName() {
        return arabic_name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setEnglishName(String english_name) {
        this.english_name = english_name;
    }

    public void setArabicName(String arabic_name) {
        this.arabic_name = arabic_name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
