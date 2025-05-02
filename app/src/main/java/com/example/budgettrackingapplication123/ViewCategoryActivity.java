package com.example.budgettrackingapplication123;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ViewCategoryActivity extends AppCompatActivity {

    EditText startDate, endDate;
    Button btnFetch;
    RecyclerView categoryRecyclerView;
    ExpenseSqlite db;

    CategoryAdapter adapter;

    String startDateStr = "", endDateStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        db = new ExpenseSqlite(this);

        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        btnFetch = findViewById(R.id.btnFetch);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        startDate.setOnClickListener(v -> showDatePicker(true));
        endDate.setOnClickListener(v -> showDatePicker(false));

        btnFetch.setOnClickListener(v -> {
            if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                Toast.makeText(this, "Select both dates", Toast.LENGTH_SHORT).show();
                return;
            }

            List<CategoryTotal> totals = db.getCategoryTotals(startDateStr, endDateStr);
            adapter = new CategoryAdapter(totals);
            categoryRecyclerView.setAdapter(adapter);
        });
    }

    private void showDatePicker(boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            if (isStart) {
                startDateStr = dateStr;
                startDate.setText(dateStr);
            } else {
                endDateStr = dateStr;
                endDate.setText(dateStr);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
