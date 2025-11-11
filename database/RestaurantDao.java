package com.example.restaurantpos.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert
    void insertMenuItem(MenuItem item);

    @Query("SELECT * FROM menu_items ORDER BY name ASC")
    LiveData<List<MenuItem>> getAllMenuItems();

    @Insert
    void insertOrder(Order order);

    @Query("SELECT * FROM orders WHERE timestamp >= :dayStart AND timestamp < :dayEnd ORDER BY timestamp DESC")
    LiveData<List<Order>> getOrdersForDay(long dayStart, long dayEnd);
}
