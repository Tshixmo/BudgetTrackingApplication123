package com.example.budgettrackingapplication123;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.budgettrackingapplication123.R;



import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<CategoryTotal> categoryList;

    public CategoryAdapter(List<CategoryTotal> categoryList) {
        this.categoryList = categoryList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, categoryTotal;

        public ViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.categoryName);
            categoryTotal = view.findViewById(R.id.categoryTotal);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_total, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryTotal ct = categoryList.get(position);
        holder.categoryName.setText(ct.getCategory());
        holder.categoryTotal.setText("R" + String.format("%.2f", ct.getTotal()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
