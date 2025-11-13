package com.example.ordermng.database;

import android.content.Context;

import androidx.annotation.NonNull; 
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase; 

import com.example.ordermng.dao.MenuItemDao;
import com.example.ordermng.dao.OrderDao;
import com.example.ordermng.dao.OrderItemDao;
import com.example.ordermng.model.Converters;
import com.example.ordermng.model.CustomerOrder;
import com.example.ordermng.model.MenuItem;
import com.example.ordermng.model.OrderItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MenuItem.class, CustomerOrder.class, OrderItem.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MenuItemDao menuItemDao();
    public abstract OrderDao orderDao();
    public abstract OrderItemDao orderItemDao();

    private static volatile AppDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "ordermng_database")
                            .addCallback(roomCallback) 
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {

                MenuItemDao dao = INSTANCE.menuItemDao();

                dao.insert(new MenuItem("Buff Momo Full", 100.0));
                dao.insert(new MenuItem("Buff Momo Half", 70.0));
                dao.insert(new MenuItem("Buff Chowmein Full", 120.0));
                dao.insert(new MenuItem("Buff Chowmein Half", 80.0));
                dao.insert(new MenuItem("Buff Thukpa Full", 100.0));
                dao.insert(new MenuItem("Buff Thukpa Half", 70.0));
                dao.insert(new MenuItem("Water / Juice", 20.0));

            });
        }
    };

}
