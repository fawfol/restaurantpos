package com.example.ordermng.adapter;

import android.view.LayoutInflater;
import android.graphics.Color;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ordermng.databinding.OrderListItemBinding; 
import com.example.ordermng.model.CustomerOrder;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ActiveOrderAdapter extends ListAdapter<CustomerOrder, ActiveOrderAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(CustomerOrder order);
    }
    private OnOrderClickListener orderClickListener;

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.orderClickListener = listener;
    }

    public interface OnToggleStatusListener {
        void onToggle(CustomerOrder order);
    }
    private OnToggleStatusListener toggleListener;

    public void setOnToggleStatusListener(OnToggleStatusListener listener) {
        this.toggleListener = listener;
    }

    public ActiveOrderAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CustomerOrder> DIFF_CALLBACK = new DiffUtil.ItemCallback<CustomerOrder>() {

        @Override
        public boolean areItemsTheSame(@NonNull CustomerOrder oldItem, @NonNull CustomerOrder newItem) {
            return oldItem.orderId == newItem.orderId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CustomerOrder oldItem, @NonNull CustomerOrder newItem) {
            return oldItem.status.equals(newItem.status) &&
                    oldItem.totalAmount == newItem.totalAmount;
        }
    };

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderListItemBinding binding = OrderListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        CustomerOrder currentOrder = getItem(position);

        holder.bind(currentOrder, toggleListener, orderClickListener);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final OrderListItemBinding binding;
        private final SimpleDateFormat timeFormat;

        public OrderViewHolder(OrderListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        }

        public void bind(CustomerOrder order, OnToggleStatusListener toggleListener, OnOrderClickListener orderClickListener) {

            binding.tvOrderId.setText("Order #" + order.orderId);
            binding.tvOrderTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", order.totalAmount));
            binding.tvOrderTimestamp.setText("Time: " + timeFormat.format(order.timestamp));

            if (order.status.equals("Pending")) {
                binding.tvOrderStatus.setText("Status: Pending");
                binding.tvOrderStatus.setTextColor(Color.parseColor("#9C4A00")); 
                binding.btnToggleStatus.setText("Mark as Served");
            } else if (order.status.equals("Served")) {
                binding.tvOrderStatus.setText("Status: Served");
                binding.tvOrderStatus.setTextColor(Color.parseColor("#006E10")); 
                binding.btnToggleStatus.setText("Mark as Pending");
            }

            binding.btnToggleStatus.setOnClickListener(v -> {
                if (toggleListener != null) {
                    toggleListener.onToggle(order);
                }
            });

            itemView.setOnClickListener(v -> {
                if (orderClickListener != null) {
                    orderClickListener.onOrderClick(order);
                }
            });
        }
    }
}
