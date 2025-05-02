package com.example.budgettrackingapplication123;

import android.database.Cursor;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    public static boolean REC_VIEW = true;

    RecyclerView recyclerView;
    ExpenseAdepter adepter;
    ArrayList<ExpenseModel> arrayList = new ArrayList<>();
    ExpenseSqlite sqlite;

    TextView recyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyTv = findViewById(R.id.recyTv);
        recyclerView = findViewById(R.id.recyclerView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sqlite = new ExpenseSqlite(this);

        recyTv.setText("Expense List");
        loadData();
    }

    public void loadData() {
        Cursor cursor = sqlite.showExpenseRecyclerView();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String photo = cursor.getString(cursor.getColumnIndexOrThrow("photo"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                arrayList.add(new ExpenseModel(id, name, description, amount, category, photo, date));
            }

            adepter = new ExpenseAdepter(arrayList, RecyclerViewActivity.this);
            recyclerView.setAdapter(adepter);
        }
    }
}
