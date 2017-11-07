package com.example.mks.gadgethunter.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mks.gadgethunter.Models.Post;
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

public class ForumPost extends AppCompatActivity {
    Button postButton;
    Button upload;
    EditText postEditText, titleEditText;
    ImageView img;
    final int REQUEST_FOR_IMAGE = 1;
    Uri filePath;
    String download;
    FirebaseStorage storage;
    StorageReference storageReference;
    String postString, title;
    Boolean uploadCompleted = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);
        initialize();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post(v);

            }
        });
    }

    private void initialize() {
        postEditText = (EditText) findViewById(R.id.editText);
        titleEditText = (EditText) findViewById(R.id.title);
        postButton = (Button) findViewById(R.id.postButton);
        postEditText.setMovementMethod(new ScrollingMovementMethod());
        upload = (Button) findViewById(R.id.upload);
        img = (ImageView) findViewById(R.id.img);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    void post(View v) {
        postString = postEditText.getText().toString();
        title = titleEditText.getText().toString();
        uploadImage();

    }

    void post() {
        //pushes the post object to the firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Intent intent = getIntent();
        String postId;
        if (intent.getStringExtra("Admin Post").equals("false")) {
            postId = databaseReference.child("Posts").push().getKey();
        } else {
            postId = databaseReference.child("AdminPosts").push().getKey();
        }
        Post p = new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                , title, postString, download, "0", postId);
        Log.d("DownloadUri", download);

        if (intent.getStringExtra("Admin Post").equals("false")) {
            databaseReference.child("UsertoPost").child(FirebaseAuth.getInstance().
                    getCurrentUser().getUid()).push().setValue(postId);
            databaseReference.child("Posts").child(postId).setValue(p);
        } else
            databaseReference.child("AdminPosts").child(postId).setValue(p);
        Toast.makeText(this, "Posting as " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT);

        finish();
    }

    private void uploadImage() {
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
                    uploadCompleted = true;
                    post();

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
            uploadCompleted = true;
            download = "";
            post();
        }
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
                img.setImageBitmap(bitmap);
                Log.d("Set Image", "success");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
            }

        }
    }

}
