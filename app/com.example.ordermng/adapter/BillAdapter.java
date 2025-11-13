package com.example.ordermng.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermng.databinding.BillListItemBinding; 
import com.example.ordermng.model.OrderItem;

import java.util.Locale;

public class BillAdapter extends ListAdapter<OrderItem, BillAdapter.BillViewHolder> {

    public BillAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<OrderItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<OrderItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull OrderItem oldItem, @NonNull OrderItem newItem) {
            return oldItem.orderItemId == newItem.orderItemId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull OrderItem oldItem, @NonNull OrderItem newItem) {
            return oldItem.quantity == newItem.quantity &&
                    oldItem.itemName.equals(newItem.itemName);
        }
    };

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BillListItemBinding binding = BillListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BillViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        OrderItem currentItem = getItem(position);
        holder.bind(currentItem);
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        private final BillListItemBinding binding;

        public BillViewHolder(BillListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderItem item) {
            binding.tvBillItemQty.setText(String.format(Locale.getDefault(), "%dx", item.quantity));

            String nameAndPrice = String.format(Locale.getDefault(), "%s ($%.2f)",
                    item.itemName, item.itemPrice);
            binding.tvBillItemName.setText(nameAndPrice);

            double subtotal = item.itemPrice * item.quantity;
            binding.tvBillItemSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        }
    }
}
