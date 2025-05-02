package com.example.budgettrackingapplication123;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewExpensesActivity extends AppCompatActivity {

    ExpenseSqlite db;
    ListView listView;
    ArrayList<HashMap<String, String>> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses); // You’ll create this XML below

        listView = findViewById(R.id.expenseListView);
        db = new ExpenseSqlite(this);

        expenseList = db.getAllExpensesList(); // Instead of using getAllExpenses() with Cursor

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                expenseList,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "amount"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        listView.setAdapter(adapter);
    }
}
