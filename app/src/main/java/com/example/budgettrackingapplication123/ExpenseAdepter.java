package com.example.budgettrackingapplication123;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdepter extends RecyclerView.Adapter<ExpenseAdepter.ViewHolder> {

    ArrayList<ExpenseModel> arrayList;
    Context context;

    public ExpenseAdepter(ArrayList<ExpenseModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.data_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdepter.ViewHolder holder, int position) {
        ExpenseModel model = arrayList.get(position);
        holder.tvName.setText("Name: " + model.getName());
        holder.tvDescription.setText("Description: " + model.getDescription());
        holder.tvAmount.setText("Amount: R" + String.format("%.2f", model.getAmount()));
        holder.tvCategory.setText("Category: " + model.getCategory());
        holder.tvDate.setText("Date: " + model.getDate());

        // You could also load the photo into an ImageView here using Glide or Picasso if needed
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDescription, tvAmount, tvCategory, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
