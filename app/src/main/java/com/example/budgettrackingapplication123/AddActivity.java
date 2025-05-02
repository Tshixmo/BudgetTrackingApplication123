package com.example.budgettrackingapplication123;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    TextView buyDisplay, reasonDisplay, descDisplay, catDisplay, button, addTv, tvDate;
    EditText edBuy, edReason, edDescription, edCategory, etDate;
    ImageView imageReceipt;
    View btnUploadPhoto;

    ExpenseSqlite SQLiteOpenHelper;

    public static boolean IS_ADDING_EXPENSE = true;

    String selectedDate = "";
    String selectedImageUri = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        // Bind UI
        button = findViewById(R.id.button);
        edBuy = findViewById(R.id.edBuy);
        edReason = findViewById(R.id.edReason);
        edDescription = findViewById(R.id.edDescription);
        edCategory = findViewById(R.id.edCategory);
        buyDisplay = findViewById(R.id.buyDisplay);
        reasonDisplay = findViewById(R.id.reasonDisplay);
        descDisplay = findViewById(R.id.descDisplay);
        catDisplay = findViewById(R.id.catDisplay);
        addTv = findViewById(R.id.addTv);
        tvDate = findViewById(R.id.tvDate);
        etDate = findViewById(R.id.etDate);
        imageReceipt = findViewById(R.id.imageReceipt);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);

        SQLiteOpenHelper = new ExpenseSqlite(this);

        // Setup date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(calendar.getTime());
        tvDate.setText("Date: " + selectedDate);

        etDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(selectedYear, selectedMonth, selectedDay);
                selectedDate = sdf.format(calendar.getTime());
                etDate.setText(selectedDate);
            }, year, month, day);

            datePickerDialog.show();
        });

        // Request Permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    100
            );
        }

        // Photo upload button
        btnUploadPhoto.setOnClickListener(v -> {
            String[] options = {"Take Photo", "Choose from Gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
            builder.setTitle("Upload Receipt");
            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    takePhotoFromCamera();
                } else {
                    choosePhotoFromGallery();
                }
            });
            builder.show();
        });

        // Conditional UI
        if (IS_ADDING_EXPENSE) {
            addTv.setText("Add Expense");
            buyDisplay.setText("Amount (e.g. 50.00):");
            reasonDisplay.setText("Name of Expense:");
            descDisplay.setText("Description:");
            catDisplay.setText("Category:");
            button.setText("Add Expense");
        } else {
            addTv.setText("Add Category");
            buyDisplay.setVisibility(View.GONE);
            edBuy.setVisibility(View.GONE);
            descDisplay.setVisibility(View.GONE);
            edDescription.setVisibility(View.GONE);
            catDisplay.setVisibility(View.GONE);
            edCategory.setVisibility(View.GONE);
            tvDate.setVisibility(View.GONE);
            etDate.setVisibility(View.GONE);
            imageReceipt.setVisibility(View.GONE);
            btnUploadPhoto.setVisibility(View.GONE);
            reasonDisplay.setText("Enter New Category Name:");
            edReason.setHint("e.g., Food, Transport");
            button.setText("Create Category");
        }

        // Save button
        button.setOnClickListener(v -> {
            if (IS_ADDING_EXPENSE) {
                if (edBuy.length() > 0 && edReason.length() > 0 && edDescription.length() > 0 && edCategory.length() > 0) {
                    String name = edReason.getText().toString();
                    String description = edDescription.getText().toString();
                    double amount = Double.parseDouble(edBuy.getText().toString());
                    String category = edCategory.getText().toString();
                    String photo = selectedImageUri;

                    SQLiteOpenHelper.addExpense(name, description, amount, category, photo, selectedDate);

                    edBuy.setText("");
                    edReason.setText("");
                    edDescription.setText("");
                    edCategory.setText("");
                    etDate.setText("");
                    imageReceipt.setImageResource(0);
                    selectedImageUri = "";

                    Toast.makeText(AddActivity.this, "Expense added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (edReason.length() > 0) {
                    String categoryName = edReason.getText().toString();
                    SQLiteOpenHelper.addCategory(categoryName);
                    edReason.setText("");
                    Toast.makeText(AddActivity.this, "Category added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddActivity.this, "Please enter a category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile;
        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile("receipt_", ".jpg", storageDir);
            photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show();
        }
    }

    private void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImageUri = imageUri.toString();
                imageReceipt.setImageURI(imageUri);
            } else if (requestCode == REQUEST_CAMERA && photoUri != null) {
                selectedImageUri = photoUri.toString();
                imageReceipt.setImageURI(photoUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions denied. Some features may not work.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
