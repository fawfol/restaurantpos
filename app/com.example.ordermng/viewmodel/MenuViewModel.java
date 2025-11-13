package com.example.ordermng.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ordermng.model.MenuItem;
import com.example.ordermng.repository.OrderRepository;

import java.util.List;

public class MenuViewModel extends AndroidViewModel {

    private OrderRepository mRepository;
    private final LiveData<List<MenuItem>> mAllMenuItems;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OrderRepository(application);
        mAllMenuItems = mRepository.getAllMenuItems();
    }

    public LiveData<List<MenuItem>> getAllMenuItems() {
        return mAllMenuItems;
    }

    public void insert(MenuItem menuItem) {
        mRepository.insertMenuItem(menuItem);
    }

    public void delete(MenuItem menuItem) {
        mRepository.deleteMenuItem(menuItem);
    }
}
