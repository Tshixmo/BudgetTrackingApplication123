package com.example.budgettrackingapplication123;

public class ExpenseModel {

    private int id;
    private String name;
    private String description;
    private double amount;
    private String category;
    private String photo;  // File path or Base64 string
    private String date;   // e.g., "2025-04-30"

    public ExpenseModel(int id, String name, String description, double amount, String category, String photo, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.photo = photo;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
