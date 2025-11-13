package com.example.ordermng;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast; 

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ordermng.adapter.ActiveOrderAdapter;
import com.example.ordermng.databinding.ActivityMainBinding;
import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.viewmodel.MainViewModel;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private ActiveOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); 
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupRecyclerView();

        mainViewModel.getActiveOrders().observe(this, activeOrders -> {

            adapter.submitList(activeOrders);
        });

        binding.btnManageMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuManagementActivity.class);
            startActivity(intent);
        });

        binding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        binding.btnCreateOrder.setOnClickListener(v -> {
            createNewOrderAndNavigate();
        });
    }

    private void createNewOrderAndNavigate() {
        Intent intent = new Intent(MainActivity.this, OrderDetailsActivity.class);
        intent.putExtra("ORDER_ID", -1); 
        startActivity(intent);
    }

    private void setupRecyclerView() {
        adapter = new ActiveOrderAdapter();
        binding.recyclerViewActiveOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewActiveOrders.setAdapter(adapter);

        adapter.setOnToggleStatusListener(order -> {
            mainViewModel.toggleOrderStatus(order);
        });

        adapter.setOnOrderClickListener(order -> {
            Intent intent = new Intent(MainActivity.this, OrderDetailsActivity.class);
            intent.putExtra("ORDER_ID", order.orderId);
            startActivity(intent);
        });
    }
}
