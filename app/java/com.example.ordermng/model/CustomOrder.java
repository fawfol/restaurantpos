package com.example.ordermng.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "orders")
@TypeConverters({Converters.class}) 
public class CustomerOrder {

    @PrimaryKey(autoGenerate = true)
    public int orderId;

    public double totalAmount;
    public Date timestamp; 
    public String status; 

    public CustomerOrder(double totalAmount, Date timestamp, String status) {
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
        this.status = status;
    }

    public CustomerOrder(CustomerOrder order) {
        this.orderId = order.orderId;
        this.totalAmount = order.totalAmount;
        this.timestamp = order.timestamp;
        this.status = order.status;
    }
}
