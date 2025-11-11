package com.example.restaurantpos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OrderViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;

    public OrderViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(OrderViewModel.class)) {
            return (T) new OrderViewModel(mApplication);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
