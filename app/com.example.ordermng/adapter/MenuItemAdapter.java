package com.example.ordermng.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermng.databinding.MenuListItemBinding; 
import com.example.ordermng.model.MenuItem;

import java.util.Locale;

public class MenuItemAdapter extends ListAdapter<MenuItem, MenuItemAdapter.MenuItemViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(MenuItem menuItem);
    }
    private OnDeleteClickListener deleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public MenuItemAdapter() {
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
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MenuListItemBinding binding = MenuListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem currentItem = getItem(position);
        holder.bind(currentItem, deleteClickListener);
    }

    class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private final MenuListItemBinding binding; 

        public MenuItemViewHolder(MenuListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MenuItem menuItem, OnDeleteClickListener listener) {
            binding.tvItemName.setText(menuItem.name);

            binding.tvItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", menuItem.price));

            binding.btnDeleteItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(menuItem);
                }
            });
        }
    }
}
