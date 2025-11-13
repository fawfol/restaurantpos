package com.example.ordermng.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.ordermng.dao.MenuItemDao;
import com.example.ordermng.dao.OrderDao;
import com.example.ordermng.dao.OrderItemDao;
import com.example.ordermng.database.AppDatabase;
import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.model.MenuItem;
import com.example.ordermng.model.OrderItem; 
import java.util.List;
import java.util.Date; 
import java.util.concurrent.Callable; 
import java.util.concurrent.Future;

public class OrderRepository {

    private MenuItemDao mMenuItemDao;
    private OrderDao mOrderDao;
    private OrderItemDao mOrderItemDao;

    private LiveData<List<MenuItem>> mAllMenuItems;

    private LiveData<List<CustomerOrder>> mActiveOrders;
    private LiveData<List<CustomerOrder>> mPaidOrders; 
    private LiveData<Double> mTotalProfit;
    private LiveData<List<CustomerOrder>> mTrashOrders;
    public OrderRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);

        mMenuItemDao = db.menuItemDao();
        mOrderDao = db.orderDao();
        mOrderItemDao = db.orderItemDao();

        mAllMenuItems = mMenuItemDao.getAllMenuItems();
        mActiveOrders = mOrderDao.getActiveOrders(); 
        mPaidOrders = mOrderDao.getPaidOrders(); 
        mTotalProfit = mOrderDao.getTotalDailyProfit();
        mTrashOrders = mOrderDao.getTrashOrders();
    }

    public LiveData<List<MenuItem>> getAllMenuItems() {
        return mAllMenuItems;
    }

    public void insertMenuItem(MenuItem menuItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mMenuItemDao.insert(menuItem);
        });
    }

    public void deleteMenuItem(MenuItem menuItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mMenuItemDao.delete(menuItem);
        });
    }

    public LiveData<List<CustomerOrder>> getActiveOrders() {
        return mActiveOrders;
    }

    public LiveData<List<CustomerOrder>> getPaidOrders() {
        return mPaidOrders;
    }

    public LiveData<Double> getTotalProfit() {
        return mTotalProfit;
    }

    public void updateOrder(CustomerOrder order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mOrderDao.update(order);
        });
    }

    public Future<Long> insertOrder(CustomerOrder order) {
        Callable<Long> insertCallable = () -> mOrderDao.insert(order);
        return AppDatabase.databaseWriteExecutor.submit(insertCallable);
    }

    public LiveData<CustomerOrder> getOrderById(int orderId) {
        return mOrderDao.getOrderById(orderId);
    }

    public LiveData<List<OrderItem>> getItemsForOrder(int orderId) {
        return mOrderItemDao.getItemsForOrder(orderId);
    }

    public void deleteItemsForOrder(int orderId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mOrderItemDao.deleteItemsForOrder(orderId);
        });
    }

    public void insertOrderItems(List<OrderItem> items) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mOrderItemDao.insertAll(items);
        });
    }

    public LiveData<List<CustomerOrder>> getTrashOrders() {
        return mTrashOrders;
    }

    public void deleteOrderPermanently(CustomerOrder order) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            mOrderDao.deleteOrderById(order.orderId);
        });
    }

    public long insertOrderAndGetId(CustomerOrder order) {
        return mOrderDao.insert(order);
    }

}
