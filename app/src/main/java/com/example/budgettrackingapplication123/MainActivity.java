package com.example.budgettrackingapplication123;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView mainBalance, totalExpense, addExpense;
    Button showExpense;


    ExpenseSqlite sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sqlite = new ExpenseSqlite(MainActivity.this);

        mainBalance = findViewById(R.id.mainBalance);
        totalExpense = findViewById(R.id.totalExpense);
        addExpense = findViewById(R.id.addExpense);
        showExpense = findViewById(R.id.expenseShow);

        // Launch AddActivity to add a new expense
        addExpense.setOnClickListener(v -> {
            AddActivity.IS_ADDING_EXPENSE = true;
            startActivity(new Intent(MainActivity.this, AddActivity.class));
        });

        Button addIncome = findViewById(R.id.addIncome);
        addIncome.setOnClickListener(v -> {
            AddActivity.IS_ADDING_EXPENSE = false; // Set to category mode
            startActivity(new Intent(MainActivity.this, AddActivity.class));
        });

        Button btnViewCategory = findViewById(R.id.btnViewCategory);

        btnViewCategory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewCategoryActivity.class);
            startActivity(intent);
        });


        // addIncome.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View v) {
             //   Intent intent = new Intent(MainActivity.this, AddActivity.class);
               // startActivity(intent);
            //}
        //});

        showExpense = findViewById(R.id.expenseShow);
        showExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecyclerViewActivity.class);
                startActivity(intent);
            }
        });


        // Launch RecyclerViewActivity to show all expenses
        //showExpense.setOnClickListener(v -> {
            //RecyclerViewActivity.REC_VIEW = true; // true for expenses
            //startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
            //Intent intent = new Intent(MainActivity.this, ViewExpensesActivity.class);
            //startActivity(intent);
            //Intent intent = new Intent(CurrentActivity.this, RecyclerViewActivity.class);
            //startActivity(intent);

        //});

        TextView addBudget = findViewById(R.id.addBudget);
        addBudget.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SetGoalActivity.class));
        });

        //TextView addIncome = findViewById(R.id.addIncome);
        //addBudget.setOnClickListener(v -> {
          //  startActivity(new Intent(MainActivity.this, SetGoalActivity.class));
        //});

    }

    // Refresh the main balance and total expense view
    public void showData() {
        double total = sqlite.showTotalExpenses();
        totalExpense.setText("BDT: " + total);
        mainBalance.setText("BDT: " + total); // Main balance equals total expense (since no income)
    }

    @Override
    protected void onResume() {
        super.onResume();
        showData();
    }
}
