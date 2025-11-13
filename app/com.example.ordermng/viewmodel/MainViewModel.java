package com.example.ordermng.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.repository.OrderRepository;
import java.util.List;
import java.util.Date; 
import java.util.concurrent.Future;

public class MainViewModel extends AndroidViewModel {

    private OrderRepository mRepository;
    private final LiveData<List<CustomerOrder>> mActiveOrders;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OrderRepository(application);
        mActiveOrders = mRepository.getActiveOrders();
    }

    public LiveData<List<CustomerOrder>> getActiveOrders() {
        return mActiveOrders;
    }

    public void updateOrder(CustomerOrder order) {
        mRepository.updateOrder(order);
    }

    public Future<Long> createNewOrder() {

        CustomerOrder newOrder = new CustomerOrder(
                0.0, 
                new Date(), 
                "Pending" 
        );
        return mRepository.insertOrder(newOrder);
    }

    public void toggleOrderStatus(CustomerOrder order) {

        CustomerOrder updatedOrder = new CustomerOrder(order);

        if (updatedOrder.status.equals("Pending")) {
            updatedOrder.status = "Served";
        } else if (updatedOrder.status.equals("Served")) {
            updatedOrder.status = "Pending";
        }

        mRepository.updateOrder(updatedOrder);
    }
}
