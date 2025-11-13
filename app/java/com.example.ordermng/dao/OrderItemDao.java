package com.example.ordermng.dao;

import androidx.lifecycle.LiveData; 
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.ordermng.model.OrderItem;
import java.util.List;

@Dao
public interface OrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<OrderItem> orderItems);

    @Query("SELECT * FROM order_items WHERE parentOrderId = :orderId")
    LiveData<List<OrderItem>> getItemsForOrder(int orderId);

    @Query("DELETE FROM order_items WHERE parentOrderId = :orderId")
    void deleteItemsForOrder(int orderId);
}
