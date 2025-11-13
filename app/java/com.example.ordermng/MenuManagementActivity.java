package com.example.ordermng;

import android.os.Bundle;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ordermng.adapter.MenuItemAdapter;
import com.example.ordermng.databinding.ActivityMenuManagementBinding; 
import com.example.ordermng.model.MenuItem;
import com.example.ordermng.viewmodel.MenuViewModel;

public class MenuManagementActivity extends AppCompatActivity {

    private ActivityMenuManagementBinding binding; 
    private MenuViewModel menuViewModel; 
    private MenuItemAdapter adapter; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); 
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        setupRecyclerView();

        menuViewModel.getAllMenuItems().observe(this, menuItems -> {
            adapter.submitList(menuItems);
        });

        binding.btnAddItem.setOnClickListener(v -> {
            addItem();
        });

        adapter.setOnDeleteClickListener(menuItem -> {
            menuViewModel.delete(menuItem);
            Toast.makeText(this, "Deleted " + menuItem.name, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupRecyclerView() {
        adapter = new MenuItemAdapter();
        binding.recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMenu.setAdapter(adapter);

        binding.recyclerViewMenu.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    private void addItem() {
        String name = binding.etItemName.getText().toString().trim();
        String priceStr = binding.etItemPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please enter name and price", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            MenuItem menuItem = new MenuItem(name, price);

            menuViewModel.insert(menuItem);

            Toast.makeText(this, "Added " + name, Toast.LENGTH_SHORT).show();

            binding.etItemName.setText("");
            binding.etItemPrice.setText("");
            binding.etItemName.requestFocus();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
        }
    }
}
