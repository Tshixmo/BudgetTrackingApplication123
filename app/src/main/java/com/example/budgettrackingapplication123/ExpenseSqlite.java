package com.example.budgettrackingapplication123;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ExpenseSqlite extends SQLiteOpenHelper {

    public ExpenseSqlite(@Nullable Context context) {
        super(context, "DATABASE_LEMON", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE expense (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "description TEXT," +
                "amount DOUBLE," +
                "category TEXT," +
                "photo TEXT," +
                "date TEXT)");

        database.execSQL("CREATE TABLE category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)");

        // ✅ USERS TABLE
        database.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS expense");
        database.execSQL("DROP TABLE IF EXISTS category");
        database.execSQL("DROP TABLE IF EXISTS users"); // for login/signup
        onCreate(database);
    }

    public void addExpense(String name, String description, double amount, String category, String photo, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("amount", amount);
        values.put("category", category);
        values.put("photo", photo);
        values.put("date", date);
        database.insert("expense", null, values);
        database.close();
    }

    public void addCategory(String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        database.insert("category", null, values);
        database.close();
    }

    public Cursor getAllExpenses() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM expense", null);
    }

    public Cursor getAllCategories() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM category", null);
    }

    public double getCategoryTotal(String categoryName) {
        double total = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT amount FROM expense WHERE category = ?", new String[]{categoryName});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                total += cursor.getDouble(0);
            }
            cursor.close();
        }
        return total;
    }

    public List<CategoryTotal> getCategoryTotals(String startDate, String endDate) {
        List<CategoryTotal> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT category, SUM(amount) as total FROM expense WHERE date BETWEEN ? AND ? GROUP BY category";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                list.add(new CategoryTotal(category, total));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public double showTotalExpenses() {
        double totalExpense = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT amount FROM expense", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                totalExpense += cursor.getDouble(0);
            }
            cursor.close();
        }
        return totalExpense;
    }

    public Cursor showExpenseRecyclerView() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM expense", null);
    }

    public ArrayList<HashMap<String, String>> getAllExpensesList() {
        ArrayList<HashMap<String, String>> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, amount FROM expense", null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", cursor.getString(0));
                map.put("amount", "R" + cursor.getString(1));
                expenseList.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

    // ✅ Register user
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    // ✅ Login user
    public boolean checkUserLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }
}
