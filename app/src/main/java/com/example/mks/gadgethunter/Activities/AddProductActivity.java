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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mks.gadgethunter.Models.Product;
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

public class AddProductActivity extends AppCompatActivity {

    Spinner categorySpinner;
    Spinner availabilitySpinner;
    EditText productName, productPrice, productDescription, productBrand;
    ImageView productImage;
    String category, name, availability, productId, description, brand;
    String price;
    DatabaseReference databaseReference;
    final int REQUEST_FOR_IMAGE = 1;
    private Uri filePath;
    private String download;
    private StorageReference storageReference;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        System.out.println("price=" + price);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        productImage = (ImageView) findViewById(R.id.product_image);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        availabilitySpinner = (Spinner) findViewById(R.id.availibility_spinner);
        productName = (EditText) findViewById(R.id.product_name_edit);
        productPrice = (EditText) findViewById(R.id.product_price_edit);
        productBrand = (EditText) findViewById(R.id.product_brand_edit);
        productDescription = (EditText) findViewById(R.id.product_description_edit);
        productDescription.setMovementMethod(new ScrollingMovementMethod());

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ShopName");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterAvail = ArrayAdapter.createFromResource(this,
                R.array.yes_no, android.R.layout.simple_spinner_item);
        adapterAvail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        availabilitySpinner.setAdapter(adapterAvail);
    }


    private int GetText() {
        category = categorySpinner.getSelectedItem().toString();
        availability = availabilitySpinner.getSelectedItem().toString();
        name = productName.getText().toString();
        if (name.trim().length() < 1) {
            Toast.makeText(getApplicationContext(), "Enter a name for product", Toast.LENGTH_SHORT);
            Log.d("namelo", name);
            return 0;
        }
        price = productPrice.getText().toString();
        try {
            Integer.parseInt(price);
        } catch (Exception e) {
            Toast.makeText(this, "Enter a valid price for product", Toast.LENGTH_SHORT);
            return 0;
        }
        description = productDescription.getText().toString();
        if (description.trim().length() < 1) {
            Toast.makeText(this, "Enter a description for product", Toast.LENGTH_SHORT);
            return 0;
        }
        brand = productBrand.getText().toString();
        if (brand.trim().length() < 1) {
            Toast.makeText(this, "Enter a brand for product", Toast.LENGTH_SHORT);
            return 0;
        }
        return 1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_close:
                this.finish();
                return true;

            case R.id.action_done:

                if (GetText() == 1) {
                    writeNewProduct(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),
                            name, category, price, availability, description, brand);

                }

                return true;


            default:
                // If we got here, the Product's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_done, menu);
        return true;
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
                productImage.setImageBitmap(bitmap);
                Log.d("Set Image", "success");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
            }

        }
    }

    private void writeNewProductHelper(String ShopId, String name, String category, String price, String availability, String imageUrl, String description, String brand) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        productId = mDatabase.child("Shops").child(ShopId).push().getKey();
        Product product = new Product(name, category, availability, price, imageUrl, productId, description, brand);
        mDatabase.child("Shops").child(ShopId).child(productId).setValue(product);
        Log.d("UserName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        finish();
        //  mDatabase.child("ShopName").child(ShopId).setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    private void writeNewProduct(String ShopId,
                                 String name, String category,
                                 String price, String availability, String description, String brand) {
        uploadImage(ShopId, name, category, price, availability, description, brand);


    }

    private void uploadImage(final String ShopId, final String name,
                             final String category, final String price,
                             final String availability, final String description,
                             final String brand) {
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
                    writeNewProductHelper(ShopId, name, category, price, availability, download, description, brand);


                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "upload failed" + e.getMessage(), Toast.LENGTH_SHORT);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") double progess = 100.0 * ((double)
                            taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded: " + (int) progess + "%");
                }
            });

        } else {
            writeNewProductHelper(ShopId, name, category, price, availability, "", description, brand);

        }
    }

}

