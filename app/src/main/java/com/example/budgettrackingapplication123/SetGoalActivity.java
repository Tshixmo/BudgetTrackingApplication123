package com.example.budgettrackingapplication123;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetGoalActivity extends AppCompatActivity {

    EditText etMinGoal, etMaxGoal;
    Button btnSaveGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

        etMinGoal = findViewById(R.id.etMinGoal);
        etMaxGoal = findViewById(R.id.etMaxGoal);
        btnSaveGoal = findViewById(R.id.btnSaveGoal);

        btnSaveGoal.setOnClickListener(v -> {
            String minStr = etMinGoal.getText().toString().trim();
            String maxStr = etMaxGoal.getText().toString().trim();

            if (!minStr.isEmpty() && !maxStr.isEmpty()) {
                SharedPreferences prefs = getSharedPreferences("BudgetPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("minGoal", Float.parseFloat(minStr));
                editor.putFloat("maxGoal", Float.parseFloat(maxStr));
                editor.apply();

                Toast.makeText(this, "Goals Saved!", Toast.LENGTH_SHORT).show();
                finish(); // Close and return to MainActivity
            } else {
                Toast.makeText(this, "Please enter both values", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
