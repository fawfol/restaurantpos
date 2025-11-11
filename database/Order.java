package com.example.restaurantpos.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    public long orderId;
    public String tableNumber;
    public String customerName;
    public double totalAmount;
    public long timestamp;
}
