package com.example.ardutooth.adapters;

public class ListViewItem {
    private String name;
    private int image_id;

    public ListViewItem(String name , int image_id){
        this.name = name;
        this.image_id = image_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
