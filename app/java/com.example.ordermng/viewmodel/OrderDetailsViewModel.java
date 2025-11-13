package com.example.ordermng.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ordermng.database.AppDatabase;
import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.model.MenuItem;
import com.example.ordermng.model.OrderItem;
import com.example.ordermng.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailsViewModel extends AndroidViewModel {

    private OrderRepository mRepository;
    private int currentOrderId;

    private LiveData<List<MenuItem>> mAllMenuItems;
    private LiveData<CustomerOrder> mCurrentOrder;
    private LiveData<List<OrderItem>> mDbOrderItems; 

    private MutableLiveData<ArrayList<OrderItem>> mCartItems = new MutableLiveData<>(new ArrayList<>());
    private MediatorLiveData<List<OrderItem>> mLiveCart = new MediatorLiveData<>();
    private boolean dbItemsLoaded = false;

    public OrderDetailsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OrderRepository(application);
        mAllMenuItems = mRepository.getAllMenuItems();
    }

    public void loadOrder(int orderId) {
        this.currentOrderId = orderId;
        this.dbItemsLoaded = false; 

        if (orderId == -1) {

            mCurrentOrder = new MutableLiveData<>(
                    new CustomerOrder(0.0, new Date(), "Pending")
            );
            dbItemsLoaded = true;
        } else {

            this.mCurrentOrder = mRepository.getOrderById(orderId);

            this.mDbOrderItems = mRepository.getItemsForOrder(orderId);

            mLiveCart.addSource(mDbOrderItems, dbItems -> {
                if (dbItems != null && !dbItemsLoaded) {
                    mCartItems.setValue(new ArrayList<>(dbItems));
                    dbItemsLoaded = true;
                    mLiveCart.removeSource(mDbOrderItems);
                }
            });

        }
        mLiveCart.addSource(mCartItems, cartList -> mLiveCart.setValue(cartList));
    }

    public LiveData<List<MenuItem>> getAllMenuItems() { return mAllMenuItems; }
    public LiveData<CustomerOrder> getCurrentOrder() { return mCurrentOrder; }
    public LiveData<List<OrderItem>> getCartItems() { return mLiveCart; }

    public void addItemToCart(MenuItem menuItem) {
        ArrayList<OrderItem> oldCart = mCartItems.getValue();
        if (oldCart == null) oldCart = new ArrayList<>();

        ArrayList<OrderItem> newCart = new ArrayList<>();
        for (OrderItem item : oldCart) {
            newCart.add(new OrderItem(item));
        }

        boolean itemFound = false;
        for (OrderItem item : newCart) {
            if (item.itemName.equals(menuItem.name)) {
                item.quantity++;
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            OrderItem newItem = new OrderItem(currentOrderId, menuItem.name, menuItem.price, 1);
            newCart.add(newItem);
        }

        mCartItems.setValue(newCart);
    }

    public void updateCartItemQuantity(OrderItem itemToUpdate, int newQuantity) {
        ArrayList<OrderItem> oldCart = mCartItems.getValue();
        if (oldCart == null) return;

        ArrayList<OrderItem> newCart = new ArrayList<>();

        for (OrderItem item : oldCart) {
            if (item.itemName.equals(itemToUpdate.itemName)) {
                if (newQuantity > 0) {
                    OrderItem updatedItem = new OrderItem(item);
                    updatedItem.quantity = newQuantity;
                    newCart.add(updatedItem);
                }
            } else {
                newCart.add(new OrderItem(item));
            }
        }

        mCartItems.setValue(newCart);
    }

    public void saveCartToDatabase(String newStatus) {
        ArrayList<OrderItem> cart = mCartItems.getValue();
        if (cart == null) return;

        if (currentOrderId == -1 && cart.isEmpty()) {
            return;
        }

        double newTotal = 0;
        for(OrderItem item : cart) {
            newTotal += item.itemPrice * item.quantity;
        }

        CustomerOrder order = mCurrentOrder.getValue();
        if (order == null) return;

        order.totalAmount = newTotal;
        order.status = newStatus;

        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (currentOrderId == -1) {
                long newOrderId = mRepository.insertOrderAndGetId(order);
                for (OrderItem cartItem : cart) {
                    cartItem.parentOrderId = (int) newOrderId;
                }
                if (!cart.isEmpty()) {
                    mRepository.insertOrderItems(cart);
                }
            } else {
                for (OrderItem cartItem : cart) {
                    cartItem.parentOrderId = currentOrderId;
                }
                mRepository.updateOrder(order);
                mRepository.deleteItemsForOrder(currentOrderId);
                if (!cart.isEmpty()) {
                    mRepository.insertOrderItems(cart);
                }
            }
        });
    }

    public void markOrderAsPaid() {
        saveCartToDatabase("Paid");
    }

    public void saveCartAsPending() {
        saveCartToDatabase("Pending");
    }
}
