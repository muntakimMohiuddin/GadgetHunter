package com.example.mks.gadgethunter.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mks.gadgethunter.Models.Shops;
import com.example.mks.gadgethunter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.content.Intent.createChooser;

public class AddShop extends AppCompatActivity {
    Button addImage, addShop;
    ImageView shopImage;
    EditText shopName;
    DatabaseReference databaseReference;
    final int REQUEST_FOR_IMAGE = 1;
    private Uri filePath;
    private String download;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        initialize();
        addListener();
    }

    private void initialize() {
        shopName = (EditText) findViewById(R.id.shop_name);
        shopImage = (ImageView) findViewById(R.id.shop_image);
        addImage = (Button) findViewById(R.id.add_image);
        addShop = (Button) findViewById(R.id.add_shop);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ShopName");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void addListener() {
        addShop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String shopname = shopName.getText().toString();
                uploadImage(shopname);
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        //prompts the user to select an image file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(createChooser(intent, "Select Picture"), REQUEST_FOR_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //invoked when the user selects an image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_IMAGE && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d("Load Image", "success");
                shopImage.setImageBitmap(bitmap);
                Log.d("Set Image", "success");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
            }

        }
    }

    private void uploadImage(final String shopname) {
        //uploads the photo selected by the user
        //shows progress of the upload
        //stores an URL for the uploaded photo
        //invokes the post() method to push the contents of the post

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("uploading");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    download = new String(downloadUrl.toString());
                    databaseReference.child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).setValue(new Shops(shopname, download));
                    Log.d("shopadd", shopname);
                    startActivity(new Intent(AddShop.this, AddProductActivity.class));
                    finish();

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "upload failed" + e.getMessage(), Toast.LENGTH_SHORT);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") double progess = 100.0 * ((double) taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded: " + (int) progess + "%");
                }
            });

        } else {
            databaseReference.child(FirebaseAuth.getInstance()
                    .getCurrentUser().getUid()).setValue(new Shops(shopname, ""));
            startActivity(new Intent(AddShop.this, AddProductActivity.class));
            finish();
        }
    }

}
