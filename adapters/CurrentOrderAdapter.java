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
import java.util.Map;

public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.OrderViewHolder> {

    private List<Map.Entry<MenuItem, Integer>> orderItems = new ArrayList<>();
    private OnItemClickListener listener;

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOrderItemName;
        public TextView textViewOrderItemDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderItemName = itemView.findViewById(R.id.textView_orderItemName);
            textViewOrderItemDetails = itemView.findViewById(R.id.textView_orderItemDetails);
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
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Map.Entry<MenuItem, Integer> currentEntry = orderItems.get(position);
        MenuItem item = currentEntry.getKey();
        int quantity = currentEntry.getValue();

        holder.textViewOrderItemName.setText(item.name);
        holder.textViewOrderItemDetails.setText(
                quantity + " x ₹" + String.format("%.2f", item.price)
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public void setOrderItems(Map<MenuItem, Integer> orderMap) {
        this.orderItems = new ArrayList<>(orderMap.entrySet());
        notifyDataSetChanged();
    }
}
