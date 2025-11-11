package com.example.restaurantpos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantpos.R;
import com.example.restaurantpos.database.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private List<MenuItem> menuItems = new ArrayList<>();
    private OnItemClickListener listener;

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewItemName;
        public TextView textViewItemPrice;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textView_itemName);
            textViewItemPrice = itemView.findViewById(R.id.textView_itemPrice);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_menu, parent, false);
        return new MenuItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem currentItem = menuItems.get(position);
        holder.textViewItemName.setText(currentItem.name);
        holder.textViewItemPrice.setText("₹" + String.format("%.2f", currentItem.price));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setMenuItems(List<MenuItem> items) {
        this.menuItems = items;
        notifyDataSetChanged();
    }
}
