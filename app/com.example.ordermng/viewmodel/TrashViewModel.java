package com.example.ordermng.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.core.graphics.Insets;
import androidx.lifecycle.LiveData;
import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.repository.OrderRepository;
import java.util.List;

public class TrashViewModel extends AndroidViewModel {

    private OrderRepository mRepository;
    private final LiveData<List<CustomerOrder>> mTrashOrders;

    public TrashViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OrderRepository(application);
        mTrashOrders = mRepository.getTrashOrders();
    }

    public LiveData<List<CustomerOrder>> getTrashOrders() {
        return mTrashOrders;
    }

    public void recoverOrder(CustomerOrder order) {
        order.status = "Paid";
        mRepository.updateOrder(order);
    }

    public void deleteOrderPermanently(CustomerOrder order) {
        mRepository.deleteOrderPermanently(order);
    }
}
