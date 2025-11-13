package com.example.ordermng.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ordermng.model.CustomerOrder;
import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    long insert(CustomerOrder order); // Returns the new orderId (as a long)

    @Update
    void update(CustomerOrder order);

    @Query("DELETE FROM orders WHERE orderId = :orderId")
    void deleteOrderById(int orderId);

    // For MainActivity (Pending & Served)
    @Query("SELECT * FROM orders WHERE status = 'Pending' OR status = 'Served' ORDER BY timestamp DESC")
    LiveData<List<CustomerOrder>> getActiveOrders();

    // For HistoryActivity (Paid)
    @Query("SELECT * FROM orders WHERE status = 'Paid' ORDER BY timestamp DESC")
    LiveData<List<CustomerOrder>> getPaidOrders();

    // For TrashActivity (Trash)
    @Query("SELECT * FROM orders WHERE status = 'Trash' ORDER BY timestamp DESC")
    LiveData<List<CustomerOrder>> getTrashOrders();

    // For getting a single order (when opening OrderDetailsActivity)
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    LiveData<CustomerOrder> getOrderById(int orderId);

    // For calculating total profit in HistoryActivity
    @Query("SELECT SUM(totalAmount) FROM orders WHERE status = 'Paid'")
    LiveData<Double> getTotalDailyProfit(); // We'll need to query by date later, but this is a start
}
