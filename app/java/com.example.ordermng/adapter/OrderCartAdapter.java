package com.example.ordermng.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermng.databinding.OrderCartItemBinding; 
import com.example.ordermng.model.OrderItem;

import java.util.Locale;

public class OrderCartAdapter extends ListAdapter<OrderItem, OrderCartAdapter.CartViewHolder> {

    public interface OnCartItemListener {
        void onIncreaseQuantity(OrderItem item);
        void onDecreaseQuantity(OrderItem item);
    }
    private OnCartItemListener cartListener;

    public void setOnCartItemListener(OnCartItemListener listener) {
        this.cartListener = listener;
    }

    public OrderCartAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<OrderItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<OrderItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull OrderItem oldItem, @NonNull OrderItem newItem) {

            return oldItem.itemName.equals(newItem.itemName);
        }
        @Override
        public boolean areContentsTheSame(@NonNull OrderItem oldItem, @NonNull OrderItem newItem) {

            return oldItem.quantity == newItem.quantity && oldItem.itemPrice == newItem.itemPrice;
        }
    };

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderCartItemBinding binding = OrderCartItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderItem currentItem = getItem(position);
        holder.bind(currentItem, cartListener);
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final OrderCartItemBinding binding;

        public CartViewHolder(OrderCartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderItem item, OnCartItemListener listener) {
            binding.tvItemName.setText(item.itemName);
            binding.tvQuantity.setText(String.valueOf(item.quantity));

            double subtotal = item.itemPrice * item.quantity;
            String details = String.format(Locale.getDefault(), "%d x $%.2f = $%.2f",
                    item.quantity, item.itemPrice, subtotal);
            binding.tvItemDetails.setText(details);

            binding.btnIncrease.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIncreaseQuantity(item);
                }
            });

            binding.btnDecrease.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDecreaseQuantity(item);
                }
            });
        }
    }
}
