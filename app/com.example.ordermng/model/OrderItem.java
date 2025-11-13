package com.example.ordermng.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index; 
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items",
        foreignKeys = @ForeignKey(entity = CustomerOrder.class,
                parentColumns = "orderId",
                childColumns = "parentOrderId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "parentOrderId")}) 
public class OrderItem {

    @PrimaryKey(autoGenerate = true)
    public int orderItemId;

    public int parentOrderId;
    public String itemName;
    public double itemPrice;
    public int quantity;

    public OrderItem(int parentOrderId, String itemName, double itemPrice, int quantity) {
        this.parentOrderId = parentOrderId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    public OrderItem(OrderItem item) {
        this.orderItemId = item.orderItemId;
        this.parentOrderId = item.parentOrderId;
        this.itemName = item.itemName;
        this.itemPrice = item.itemPrice;
        this.quantity = item.quantity;
    }
}
