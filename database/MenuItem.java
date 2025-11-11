package com.example.restaurantpos.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menu_items")
public class MenuItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double price;
}
