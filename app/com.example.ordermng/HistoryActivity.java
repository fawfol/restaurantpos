package com.example.ordermng;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ordermng.adapter.HistoryOrderAdapter;
import com.example.ordermng.databinding.ActivityHistoryBinding; 
import com.example.ordermng.viewmodel.HistoryViewModel;

import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding; 
    private HistoryViewModel viewModel;
    private HistoryOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); 
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        setupRecyclerView();

        setupObservers();

        binding.btnGoToTrash.setOnClickListener(v -> {

            Intent intent = new Intent(HistoryActivity.this, TrashActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        adapter = new HistoryOrderAdapter();
        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewHistory.setAdapter(adapter);

        adapter.setOnOrderClickListener(order -> {
            Intent intent = new Intent(HistoryActivity.this, OrderDetailsActivity.class);
            intent.putExtra("ORDER_ID", order.orderId);
            startActivity(intent);
        });

        adapter.setOnDeleteClickListener(order -> {
            viewModel.moveOrderToTrash(order);
            Toast.makeText(this, "Order #" + order.orderId + " moved to trash", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupObservers() {

        viewModel.getPaidOrders().observe(this, paidOrders -> {
            adapter.submitList(paidOrders);
        });

        viewModel.getTotalProfit().observe(this, total -> {
            if (total == null) {
                total = 0.0;
            }
            binding.tvTotalProfit.setText(
                    String.format(Locale.getDefault(), "Total Profit: $%.2f", total)
            );
        });
    }
}
