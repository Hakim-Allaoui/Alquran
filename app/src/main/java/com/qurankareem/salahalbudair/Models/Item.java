package com.qurankareem.salahalbudair.Models;

public class Item {

    private String title;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Item(String title,int id) {
        this.title = title;
        this.id = id;
    }
}
