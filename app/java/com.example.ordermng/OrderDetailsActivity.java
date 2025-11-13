package com.example.ordermng;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets; 
import androidx.core.view.ViewCompat; 
import androidx.core.view.WindowInsetsCompat; 
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ordermng.adapter.OrderCartAdapter;
import com.example.ordermng.adapter.OrderMenuAdapter;
import com.example.ordermng.databinding.ActivityOrderDetailsBinding;
import com.example.ordermng.model.OrderItem;
import com.example.ordermng.viewmodel.OrderDetailsViewModel;

import java.util.List;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    private ActivityOrderDetailsBinding binding;
    private OrderDetailsViewModel viewModel;
    private OrderMenuAdapter menuAdapter;
    private OrderCartAdapter cartAdapter;
    private int currentOrderId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);

        currentOrderId = getIntent().getIntExtra("ORDER_ID", -1);

        viewModel.loadOrder(currentOrderId);

        setupMenuRecyclerView();
        setupCartRecyclerView();

        setupObservers();

        setupClickListeners();
    }

    private void setupMenuRecyclerView() {
        menuAdapter = new OrderMenuAdapter();
        binding.recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMenu.setAdapter(menuAdapter);

        menuAdapter.setOnMenuItemClickListener(menuItem -> {
            viewModel.addItemToCart(menuItem);
        });
    }

    private void setupCartRecyclerView() {
        cartAdapter = new OrderCartAdapter();
        binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCart.setAdapter(cartAdapter);

        cartAdapter.setOnCartItemListener(new OrderCartAdapter.OnCartItemListener() {
            @Override
            public void onIncreaseQuantity(OrderItem item) {
                viewModel.updateCartItemQuantity(item, item.quantity + 1);
            }

            @Override
            public void onDecreaseQuantity(OrderItem item) {
                viewModel.updateCartItemQuantity(item, item.quantity - 1);
            }
        });
    }

    private void setupObservers() {

        viewModel.getAllMenuItems().observe(this, menuItems -> {
            menuAdapter.submitList(menuItems);
        });

        viewModel.getCurrentOrder().observe(this, order -> {
            if (order != null) {

                if (order.orderId == 0) {
                    binding.tvOrderId.setText("New Order");
                } else {
                    binding.tvOrderId.setText("Order #" + order.orderId);
                }
            }
        });

        viewModel.getCartItems().observe(this, cartItems -> {
            cartAdapter.submitList(cartItems);
            calculateAndDisplayTotal(cartItems);
        });
    }

    private void calculateAndDisplayTotal(List<OrderItem> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (OrderItem item : cartItems) {
                total += item.itemPrice * item.quantity;
            }
        }
        binding.tvOrderTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
    }

    private void setupClickListeners() {

        binding.btnSaveOrder.setOnClickListener(v -> {
            viewModel.saveCartAsPending();
            Toast.makeText(this, "Order Saved", Toast.LENGTH_SHORT).show();
            finish(); 
        });

        binding.btnMarkAsPaid.setOnClickListener(v -> {
            viewModel.markOrderAsPaid();
            Toast.makeText(this, "Order marked as Paid", Toast.LENGTH_SHORT).show();
            finish(); 
        });

        binding.btnPrintBill.setOnClickListener(v -> {

            if (currentOrderId == -1 && viewModel.getCartItems().getValue().isEmpty()) {
                Toast.makeText(this, "Add items before printing", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentOrderId == -1) {

                Toast.makeText(this, "Save the order first to get an Order ID", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(OrderDetailsActivity.this, BillActivity.class);
                intent.putExtra("ORDER_ID", currentOrderId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Saving order...", Toast.LENGTH_SHORT).show();
        viewModel.saveCartAsPending();
        super.onBackPressed(); 
    }
}
