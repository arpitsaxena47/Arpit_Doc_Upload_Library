package com.arpit.doclibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.Permission;

public class DocumentUpload extends AppCompatActivity {

    int SELECT_PICTURE_FROM_GALLERY = 1;
    final int  MY_GALLERY_PERMISSION_CODE = 11;
    int SELECT_PICTURE_FROM_CAMERA = 0;
    final int MY_CAMERA_PERMISSION_CODE = 10;
    Bitmap doc;
    Context context;

    public DocumentUpload(Context context)
    {
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);

    }



    public Bitmap showDocChooser()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set profile image<");

        // add a list
        String[] animals = {"Take a picture", "Choose from gallery"};
        builder.setItems(animals, (dialog, which) -> {
            switch (which) {
                case 0:
                    onTakeCameraSelected();
                    break;
                case 1:
                     onChooseGallerySelected();
                    break;
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        return doc;
    }

        private void onTakeCameraSelected()
        {
            if (ActivityCompat.checkSelfPermission((Activity)context , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions((Activity)context , new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, SELECT_PICTURE_FROM_CAMERA);
            }

        }

        private void  onChooseGallerySelected()
        {
            if (ActivityCompat.checkSelfPermission((Activity)context , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions((Activity)context , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_GALLERY_PERMISSION_CODE);
            }
            else
            {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE_FROM_GALLERY);
            }

        }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case MY_CAMERA_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, SELECT_PICTURE_FROM_CAMERA);
                }
                else
                {
                    Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
                }
                break;

            case MY_GALLERY_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(context, "gallery permission granted", Toast.LENGTH_LONG).show();
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);

                    // pass the constant to compare it
                    // with the returned requestCode
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE_FROM_GALLERY);
                }
                else
                {
                    Toast.makeText(context, "external storage permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }

        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SELECT_PICTURE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
                doc = (Bitmap) data.getExtras().get("data");
            }
            else
            if (requestCode == SELECT_PICTURE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
                doc = (Bitmap) data.getExtras().get("data");

            }
        }

    }