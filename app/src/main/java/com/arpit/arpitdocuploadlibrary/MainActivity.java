package com.arpit.arpitdocuploadlibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE = 100;
    int SELECT_PICTURE_FROM_GALLERY = 1;
    final int  MY_GALLERY_PERMISSION_CODE = 11;
    int SELECT_PICTURE_FROM_CAMERA = 0;
    final int MY_CAMERA_PERMISSION_CODE = 10;
    static Uri docUri;
    Context context = MainActivity.this;
    ImageView imageView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         imageView = findViewById(R.id.imageView);
         button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showImagePickerOptions();
            }
        });
    }

    private  void showImagePickerOptions() {
        DocUploadActivity.showDocChooser(context, new DocUploadActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(context, DocUploadActivity.class);
        intent.putExtra(DocUploadActivity.INTENT_IMAGE_PICKER_OPTION, DocUploadActivity.REQUEST_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(context, DocUploadActivity.class);
        intent.putExtra(DocUploadActivity.INTENT_IMAGE_PICKER_OPTION, DocUploadActivity.REQUEST_GALLERY_IMAGE);

        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("uri");
                imageView.setImageURI(uri);
//                try {
//                    // You can update this bitmap to your server
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                    imageView.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            else
            {
                Toast.makeText(context, "Result Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private  void onButtonClick()
    {

    }

}