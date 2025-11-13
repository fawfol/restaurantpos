package com.example.ordermng.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermng.databinding.HistoryListItemBinding; 
import com.example.ordermng.model.CustomerOrder;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HistoryOrderAdapter extends ListAdapter<CustomerOrder, HistoryOrderAdapter.HistoryViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(CustomerOrder order);
    }
    private OnDeleteClickListener deleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public interface OnOrderClickListener {
        void onClick(CustomerOrder order);
    }
    private OnOrderClickListener orderClickListener;

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.orderClickListener = listener;
    }

    public HistoryOrderAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CustomerOrder> DIFF_CALLBACK = new DiffUtil.ItemCallback<CustomerOrder>() {
        @Override
        public boolean areItemsTheSame(@NonNull CustomerOrder oldItem, @NonNull CustomerOrder newItem) {
            return oldItem.orderId == newItem.orderId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CustomerOrder oldItem, @NonNull CustomerOrder newItem) {

            return oldItem.totalAmount == newItem.totalAmount &&
                    oldItem.timestamp.equals(newItem.timestamp);
        }
    };

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryListItemBinding binding = HistoryListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CustomerOrder currentOrder = getItem(position);
        holder.bind(currentOrder, deleteClickListener, orderClickListener);
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final HistoryListItemBinding binding;
        private final SimpleDateFormat dateTimeFormat;

        public HistoryViewHolder(HistoryListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.getDefault());
        }

        public void bind(CustomerOrder order, OnDeleteClickListener deleteListener, OnOrderClickListener orderListener) {
            binding.tvOrderId.setText("Order #" + order.orderId);
            binding.tvOrderTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", order.totalAmount));

            binding.tvOrderTimestamp.setText("Date: " + dateTimeFormat.format(order.timestamp));

            binding.btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(order);
                }
            });

            itemView.setOnClickListener(v -> {
                if (orderListener != null) {
                    orderListener.onClick(order);
                }
            });
        }
    }
}
