package com.example.ordermng.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermng.databinding.OrderMenuItemBinding; 
import com.example.ordermng.model.MenuItem;

import java.util.Locale;

public class OrderMenuAdapter extends ListAdapter<MenuItem, OrderMenuAdapter.MenuViewHolder> {

    public interface OnMenuItemClickListener {
        void onItemClick(MenuItem menuItem);
    }
    private OnMenuItemClickListener clickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.clickListener = listener;
    }

    public OrderMenuAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<MenuItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<MenuItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            return oldItem.id == newItem.id;
        }
        @Override
        public boolean areContentsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            return oldItem.name.equals(newItem.name) && oldItem.price == newItem.price;
        }
    };

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderMenuItemBinding binding = OrderMenuItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem currentItem = getItem(position);
        holder.bind(currentItem, clickListener);
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        private final OrderMenuItemBinding binding;

        public MenuViewHolder(OrderMenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MenuItem menuItem, OnMenuItemClickListener listener) {
            binding.tvItemName.setText(menuItem.name);
            binding.tvItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", menuItem.price));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(menuItem);
                }
            });
        }
    }
}
