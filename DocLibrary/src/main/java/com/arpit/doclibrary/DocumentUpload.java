package com.arpit.doclibrary;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.security.Permission;

import static androidx.core.content.FileProvider.getUriForFile;

public class DocumentUpload extends AppCompatActivity {
    public static final String INTENT_IMAGE_PICKER_OPTION = "image_picker_option";
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;


    int SELECT_PICTURE_FROM_GALLERY = 1;
    final int MY_GALLERY_PERMISSION_CODE = 11;
    int SELECT_PICTURE_FROM_CAMERA = 0;
    final int MY_CAMERA_PERMISSION_CODE = 10;
    Uri doc;
    public static String fileName;
    public Context context = DocumentUpload.this;
    ImageView imgSuccess, imgFailure;

//    public interface PickerOptionListener {
//        void onTakeCameraSelected();
//
//        void onChooseGallerySelected();
//    }

    //    public DocumentUpload(Context context)
//    {
//        this.context = context;
//    }
    public DocumentUpload() {
    }

    public interface PickerOptionListener {
        void onTakeCameraSelected();

        void onChooseGallerySelected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        imgFailure = findViewById(R.id.imgFailure);
        imgSuccess = findViewById(R.id.imgSuccess);

        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(getApplicationContext(), "Image picker option is missing!", Toast.LENGTH_LONG).show();
            return;
        }

        int requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            onTakeCameraSelected();
        } else {
            onChooseGallerySelected();
        }
    }


    public static void showDocChooser(Context context, PickerOptionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set profile image");

        // add a list
        String[] options = {"Take a picture", "Choose from gallery"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        listener.onTakeCameraSelected();
                        break;
                    case 1:
                        listener.onChooseGallerySelected();
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
//        return doc;
    }

    private void onTakeCameraSelected() {
        if (ActivityCompat.checkSelfPermission((Activity) context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            fileName = System.currentTimeMillis() + ".jpg";
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                ((Activity) context).startActivityForResult(takePictureIntent, SELECT_PICTURE_FROM_CAMERA);
            }
//            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            ((Activity) context).startActivityForResult(cameraIntent, SELECT_PICTURE_FROM_CAMERA);
        }

    }

    private void onChooseGallerySelected() {
        if (ActivityCompat.checkSelfPermission((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_GALLERY_PERMISSION_CODE);
        } else {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

            // pass the constant to compare it
            // with the returned requestCode
            ((Activity) context).startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE_FROM_GALLERY);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_CAMERA_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
                    fileName = System.currentTimeMillis() + ".jpg";
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        ((Activity) context).startActivityForResult(takePictureIntent, SELECT_PICTURE_FROM_CAMERA);
                    }
                } else {
                    Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
                }
                break;

            case MY_GALLERY_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "gallery permission granted", Toast.LENGTH_LONG).show();
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);

                    // pass the constant to compare it
                    // with the returned requestCode
                    ((Activity) context).startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE_FROM_GALLERY);
                } else {
                    Toast.makeText(context, "external storage permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE_FROM_CAMERA) {
            Intent intent = new Intent();
            if (resultCode == Activity.RESULT_OK) {
                intent.putExtra("uri", getCacheImagePath(fileName));
                setResult(Activity.RESULT_OK, intent);
            } else {
                setResult(Activity.RESULT_CANCELED, intent);
            }
            finish();
//            doc = data.getData();

        }
        if (requestCode == SELECT_PICTURE_FROM_GALLERY) {
//            doc = data.getData();
            Intent intent = new Intent();
            if (resultCode == Activity.RESULT_OK) {
                intent.putExtra("uri", data.getData());
                setResult(Activity.RESULT_OK, intent);
            } else {
                setResult(Activity.RESULT_CANCELED, intent);
            }
            finish();

        }
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(DocumentUpload.this, getPackageName() + ".provider", image);
    }

//    public void showSuccessScreen(Context c) {
//        Intent i = new Intent(c, DocumentUpload.class);
//        c.startActivity(i);
//        findViewById(R.id.imgSuccess).setVisibility(View.VISIBLE);
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                findViewById(R.id.imgSuccess).setVisibility(View.GONE);
//                Intent intent = new Intent(context, c.getClass());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                context.startActivity(intent);
//
//            } final Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                imgFailure.setVisibility(View.GONE);
////                Intent intent = new Intent((Activity) context, c.getClass());
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                context.startActivity(intent);
////
////            }
////        }, 2000);
//        }, 2000);
//
//    }
//
//    public void showFailureScreen(Context c) {
//        Intent i = new Intent(c, DocumentUpload.class);
//        context.startActivity(i);
//        this.imgFailure.setVisibility(View.VISIBLE);
//
//    }

    public void showSuccessDialog(Context c) {
        Dialog settingsDialog = new Dialog(c);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        settingsDialog.setContentView(inflater.inflate(R.layout.activity_success
                , null));
        settingsDialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                settingsDialog.dismiss();

            }
        }, 2000);

    }
    public  void showFailureDialog(Context c) {
        Dialog settingsDialog = new Dialog(c );
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        settingsDialog.setContentView(inflater.inflate(R.layout.activity_failure
                , null));
        settingsDialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                settingsDialog.dismiss();

            }
        }, 2000);


    }
}