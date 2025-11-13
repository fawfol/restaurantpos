package com.example.ordermng;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.core.graphics.Insets; 
import androidx.core.view.ViewCompat; 
import androidx.core.view.WindowInsetsCompat;

import com.example.ordermng.adapter.BillAdapter;
import com.example.ordermng.databinding.ActivityBillBinding;
import com.example.ordermng.viewmodel.BillViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class BillActivity extends AppCompatActivity {

    private ActivityBillBinding binding;
    private BillViewModel viewModel;
    private BillAdapter adapter;
    private int currentOrderId = -1;
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); 
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currentOrderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (currentOrderId == -1) {
            Toast.makeText(this, "Error: Invalid Order ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(BillViewModel.class);
        viewModel.loadOrderData(currentOrderId);

        setupRecyclerView();

        setupObservers();
    }

    private void setupRecyclerView() {
        adapter = new BillAdapter();
        binding.recyclerViewBillItems.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewBillItems.setAdapter(adapter);
    }

    private void setupObservers() {

        viewModel.getOrder().observe(this, order -> {
            if (order != null) {
                binding.tvBillOrderId.setText("Order #: " + order.orderId);
                binding.tvBillTimestamp.setText("Time: " + dateTimeFormat.format(order.timestamp));
                binding.tvBillTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", order.totalAmount));
            }
        });

        viewModel.getOrderItems().observe(this, orderItems -> {
            if (orderItems != null) {
                adapter.submitList(orderItems);
            }
        });
    }
}
