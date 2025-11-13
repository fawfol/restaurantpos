package com.example.ordermng.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menu_items")
public class MenuItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
