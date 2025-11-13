package com.example.ordermng.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.model.OrderItem;
import com.example.ordermng.repository.OrderRepository;

import java.util.List;

public class BillViewModel extends AndroidViewModel {

    private OrderRepository mRepository;
    private LiveData<CustomerOrder> mOrder;
    private LiveData<List<OrderItem>> mOrderItems;

    public BillViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OrderRepository(application);
    }

    public void loadOrderData(int orderId) {
        mOrder = mRepository.getOrderById(orderId);
        mOrderItems = mRepository.getItemsForOrder(orderId);
    }

    public LiveData<CustomerOrder> getOrder() {
        return mOrder;
    }

    public LiveData<List<OrderItem>> getOrderItems() {
        return mOrderItems;
    }
}
