package com.example.restaurantpos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.restaurantpos.database.AppDatabase;
import com.example.restaurantpos.database.MenuItem;
import com.example.restaurantpos.database.Order;
import com.example.restaurantpos.database.RestaurantDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderViewModel extends AndroidViewModel {

    private RestaurantDao dao;
    private LiveData<List<MenuItem>> allMenuItems;
    private MutableLiveData<Map<MenuItem, Integer>> currentOrder = new MutableLiveData<>();
    public LiveData<Double> orderTotal;

    public OrderViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.restaurantDao();

        allMenuItems = dao.getAllMenuItems();

        currentOrder.setValue(new HashMap<>());

        orderTotal = Transformations.map(currentOrder, (orderMap) -> {
            double total = 0.0;
            if (orderMap != null) {

                for (Map.Entry<MenuItem, Integer> entry : orderMap.entrySet()) {
                    total += entry.getKey().price * entry.getValue(); 
                }
            }
            return total;
        });
    }

    public LiveData<List<MenuItem>> getAllMenuItems() {
        return allMenuItems;
    }

    public LiveData<Map<MenuItem, Integer>> getCurrentOrder() {
        return currentOrder;
    }

    public void addItemToOrder(MenuItem item) {
        Map<MenuItem, Integer> map = currentOrder.getValue();
        if (map == null) map = new HashMap<>();

        int quantity = map.getOrDefault(item, 0);
        map.put(item, quantity + 1);

        currentOrder.setValue(map);
    }

    public void removeItemFromOrder(MenuItem item) {
        Map<MenuItem, Integer> map = currentOrder.getValue();
        if (map == null || !map.containsKey(item)) return;

        int quantity = map.get(item);
        if (quantity > 1) {
            map.put(item, quantity - 1); 
        } else {
            map.remove(item); 
        }

        currentOrder.setValue(map);
    }

    public void clearOrder() {
        currentOrder.setValue(new HashMap<>());
    }

    public void placeOrder(String tableNumber, String customerName) {
        final Map<MenuItem, Integer> orderMap = currentOrder.getValue();
        final Double total = orderTotal.getValue();

        if (orderMap == null || orderMap.isEmpty() || total == null) {
            return; 
        }

        Order newOrder = new Order();
        newOrder.tableNumber = tableNumber;
        newOrder.customerName = customerName;
        newOrder.totalAmount = total;
        newOrder.timestamp = System.currentTimeMillis(); 

        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertOrder(newOrder);

        });

        clearOrder();
    }
}
