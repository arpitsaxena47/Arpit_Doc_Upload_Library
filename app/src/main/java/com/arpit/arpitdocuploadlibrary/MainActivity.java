package com.arpit.arpitdocuploadlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.arpit.doclibrary.DocumentUpload;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentUpload documentUpload =  new DocumentUpload(MainActivity.this);
                Bitmap bitmap = documentUpload.showDocChooser();
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}