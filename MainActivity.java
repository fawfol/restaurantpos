package com.example.restaurantpos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer; 
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantpos.adapters.CurrentOrderAdapter;
import com.example.restaurantpos.adapters.MenuItemAdapter;
import com.example.restaurantpos.database.AppDatabase;
import com.example.restaurantpos.database.MenuItem;
import com.example.restaurantpos.database.RestaurantDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OrderViewModel orderViewModel;
    private MenuItemAdapter menuItemAdapter;
    private CurrentOrderAdapter currentOrderAdapter;

    private RecyclerView recyclerViewMenuItems;
    private RecyclerView recyclerViewCurrentOrder;
    private TextView textViewTotal;
    private EditText editTextTableNumber;
    private EditText editTextCustomerName;
    private Button buttonPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        OrderViewModelFactory factory = new OrderViewModelFactory(getApplication());
        orderViewModel = new ViewModelProvider(this, factory).get(OrderViewModel.class);

        recyclerViewMenuItems = findViewById(R.id.recyclerView_menuItems);
        recyclerViewCurrentOrder = findViewById(R.id.recyclerView_currentOrder);
        textViewTotal = findViewById(R.id.textView_total);
        editTextTableNumber = findViewById(R.id.editText_tableNumber);
        editTextCustomerName = findViewById(R.id.editText_customerName);
        buttonPlaceOrder = findViewById(R.id.button_placeOrder);

        setupMenuRecyclerView();
        setupCurrentOrderRecyclerView();


        setupObservers();

        addSampleDataIfNeeded();
    }

    private void setupMenuRecyclerView() {
        menuItemAdapter = new MenuItemAdapter();
        recyclerViewMenuItems.setAdapter(menuItemAdapter);
    }

    private void setupCurrentOrderRecyclerView() {
        recyclerViewCurrentOrder.setLayoutManager(new LinearLayoutManager(this));
        currentOrderAdapter = new CurrentOrderAdapter();
        recyclerViewCurrentOrder.setAdapter(currentOrderAdapter);
    }

    private void setupClickListeners() {
        menuItemAdapter.setOnItemClickListener(menuItem -> {
            orderViewModel.addItemToOrder(menuItem);
        });

        currentOrderAdapter.setOnItemClickListener(menuItem -> {
            orderViewModel.removeItemFromOrder(menuItem);
        });

        buttonPlaceOrder.setOnClickListener(v -> {
            String table = editTextTableNumber.getText().toString();
            String name = editTextCustomerName.getText().toString();

            if (table.isEmpty()) {
                Toast.makeText(this, "Please enter a table number", Toast.LENGTH_SHORT).show();
                return; 
            }

            orderViewModel.placeOrder(table, name);

            editTextTableNumber.setText("");
            editTextCustomerName.setText("");
            Toast.makeText(this, "Order Placed!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupObservers() {
        orderViewModel.getAllMenuItems().observe(this, new Observer<List<MenuItem>>() {
            @Override
            public void onChanged(List<MenuItem> menuItems) {
                menuItemAdapter.setMenuItems(menuItems);
            }
        });

        orderViewModel.getCurrentOrder().observe(this, orderMap -> {
            currentOrderAdapter.setOrderItems(orderMap);
        });

        orderViewModel.orderTotal.observe(this, total -> {
            textViewTotal.setText(String.format("Total: ₹%.2f", total));
        });
    }

    private void addSampleDataIfNeeded() {
        orderViewModel.getAllMenuItems().observe(this, new Observer<List<MenuItem>>() {
            @Override
            public void onChanged(List<MenuItem> menuItems) {
                if (menuItems == null || menuItems.isEmpty()) {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getDatabase(getApplication());
                        RestaurantDao dao = db.restaurantDao();

                        MenuItem item1 = new MenuItem();
                        item1.name = "Samosa";
                        item1.price = 20.00;
                        dao.insertMenuItem(item1);

                        MenuItem item2 = new MenuItem();
                        item2.name = "Chai";
                        item2.price = 15.00;
                        dao.insertMenuItem(item2);

                        MenuItem item3 = new MenuItem();
                        item3.name = "Lassi";
                        item3.price = 50.00;
                        dao.insertMenuItem(item3);

                        MenuItem item4 = new MenuItem();
                        item4.name = "Dosa";
                        item4.price = 80.00;
                        dao.insertMenuItem(item4);
                    });
                }
                orderViewModel.getAllMenuItems().removeObserver(this);
            }
        });
    }
}
