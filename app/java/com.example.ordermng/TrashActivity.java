package com.example.ordermng;

import android.os.Bundle;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ordermng.adapter.TrashOrderAdapter;
import com.example.ordermng.databinding.ActivityTrashBinding;
import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.viewmodel.TrashViewModel;

public class TrashActivity extends AppCompatActivity {

    private ActivityTrashBinding binding;
    private TrashViewModel viewModel;
    private TrashOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); 
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(TrashViewModel.class);

        setupRecyclerView();

        viewModel.getTrashOrders().observe(this, trashOrders -> {
            adapter.submitList(trashOrders);
        });
    }

    private void setupRecyclerView() {
        adapter = new TrashOrderAdapter();
        binding.recyclerViewTrash.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewTrash.setAdapter(adapter);

        adapter.setOnTrashInteractionListener(new TrashOrderAdapter.OnTrashInteractionListener() {
            @Override
            public void onRecover(CustomerOrder order) {
                viewModel.recoverOrder(order);
                Toast.makeText(TrashActivity.this, "Order #" + order.orderId + " recovered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeletePermanent(CustomerOrder order) {

                new AlertDialog.Builder(TrashActivity.this)
                        .setTitle("Delete Permanently?")
                        .setMessage("Are you sure you want to permanently delete Order #" + order.orderId + "? This action cannot be undone.")
                        .setPositiveButton("Yes, Delete", (dialog, which) -> {
                            viewModel.deleteOrderPermanently(order);
                            Toast.makeText(TrashActivity.this, "Order #" + order.orderId + " deleted permanently", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }
}
