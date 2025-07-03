package com.example.hw5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddEditPhotoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;

    private ImageView imageView;
    private EditText editTitle, editDescription;
    private Button btnSave;

    private DBHelper dbHelper;
    private int photoId = -1; // -1 means new
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_photo);

        imageView = findViewById(R.id.imageView);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DBHelper(this);

        // Check if editing
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            photoId = intent.getIntExtra("id", -1);
            editTitle.setText(intent.getStringExtra("title"));
            editDescription.setText(intent.getStringExtra("description"));
            imagePath = intent.getStringExtra("imagePath");

            if (imagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                imageView.setImageBitmap(bitmap);
            }
        }

        // Click image to choose
        imageView.setOnClickListener(v -> {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickIntent, PICK_IMAGE_REQUEST);
        });

        // Save button
        btnSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String desc = editDescription.getText().toString().trim();

            if (title.isEmpty() || desc.isEmpty() || imageView.getDrawable() == null) {
                Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save new image if user changed it
            if (imageView.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                imagePath = saveImageToInternalStorage(bitmap);
            }

            if (imagePath == null) {
                Toast.makeText(this, "Image saving failed", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoId == -1) {
                dbHelper.insertPhoto(title, desc, imagePath);
            } else {
                dbHelper.updatePhoto(photoId, title, desc, imagePath);
            }

            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        File dir = getFilesDir();
        String fileName = "img_" + System.currentTimeMillis() + ".jpg";
        File file = new File(dir, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
