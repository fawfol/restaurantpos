package com.example.ordermng.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.repository.OrderRepository;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private OrderRepository mRepository;
    private final LiveData<List<CustomerOrder>> mPaidOrders;
    private final LiveData<Double> mTotalProfit;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OrderRepository(application);
        mPaidOrders = mRepository.getPaidOrders();
        mTotalProfit = mRepository.getTotalProfit();
    }

    public LiveData<List<CustomerOrder>> getPaidOrders() {
        return mPaidOrders;
    }

    public LiveData<Double> getTotalProfit() {
        return mTotalProfit;
    }

    public void moveOrderToTrash(CustomerOrder order) {
        order.status = "Trash";
        mRepository.updateOrder(order);
    }
}
