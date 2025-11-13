package com.example.ordermng.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ordermng.model.MenuItem;
import java.util.List;

@Dao
public interface MenuItemDao {

    @Insert
    void insert(MenuItem menuItem);

    @Delete
    void delete(MenuItem menuItem);

    @Query("SELECT * FROM menu_items ORDER BY name ASC")
    LiveData<List<MenuItem>> getAllMenuItems();
}
