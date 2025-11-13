package com.example.ordermng.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermng.databinding.TrashListItemBinding; 
import com.example.ordermng.model.CustomerOrder;

import java.util.Locale;

public class TrashOrderAdapter extends ListAdapter<CustomerOrder, TrashOrderAdapter.TrashViewHolder> {

    public interface OnTrashInteractionListener {
        void onRecover(CustomerOrder order);
        void onDeletePermanent(CustomerOrder order);
    }
    private OnTrashInteractionListener listener;

    public void setOnTrashInteractionListener(OnTrashInteractionListener listener) {
        this.listener = listener;
    }

    public TrashOrderAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CustomerOrder> DIFF_CALLBACK = new DiffUtil.ItemCallback<CustomerOrder>() {
        @Override
        public boolean areItemsTheSame(@NonNull CustomerOrder oldItem, @NonNull CustomerOrder newItem) {
            return oldItem.orderId == newItem.orderId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CustomerOrder oldItem, @NonNull CustomerOrder newItem) {
            return oldItem.orderId == newItem.orderId;
        }
    };

    @NonNull
    @Override
    public TrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TrashListItemBinding binding = TrashListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TrashViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrashViewHolder holder, int position) {
        CustomerOrder currentOrder = getItem(position);
        holder.bind(currentOrder, listener);
    }

    class TrashViewHolder extends RecyclerView.ViewHolder {
        private final TrashListItemBinding binding;

        public TrashViewHolder(TrashListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CustomerOrder order, OnTrashInteractionListener listener) {
            binding.tvOrderId.setText("Order #" + order.orderId + " (Trashed)");
            binding.tvOrderTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", order.totalAmount));

            binding.btnRecover.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRecover(order);
                }
            });

            binding.btnDeletePermanent.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeletePermanent(order);
                }
            });
        }
    }
}
