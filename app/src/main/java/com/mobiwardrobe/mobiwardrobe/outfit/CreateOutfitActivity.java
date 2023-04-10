package com.mobiwardrobe.mobiwardrobe.outfit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobiwardrobe.mobiwardrobe.R;

import java.util.ArrayList;

public class CreateOutfitActivity extends AppCompatActivity implements CreateOutfitAdapter.CountOfImagesWhenRemoved {
    RecyclerView recyclerView;
    CreateOutfitAdapter adapter;

    EditText outfitName;
    Button saveOutfit;
    Button addToOutfit;
    Button pickImage;

    Uri imageUri;
    ArrayList<Uri> uris;
    ArrayList<String> urlsList;

    FirebaseDatabase database;
    DatabaseReference reference;

    StorageReference storagereference;
    FirebaseStorage mStorage;

    ProgressDialog progressDialog;

    private FirebaseUser firebaseUser;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_outfit);

        uris = new ArrayList<>();
        urlsList = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_show_elements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        adapter = new CreateOutfitAdapter(this, uris, this);
        recyclerView.setAdapter(adapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        outfitName = findViewById(R.id.et_outfit_name_upload);
        saveOutfit = findViewById(R.id.bt_outfit_save);
        addToOutfit = findViewById(R.id.bt_outfit_add);
        pickImage = findViewById(R.id.chooseImages);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Data");
        progressDialog.setMessage("Please Wait While Uploading Your data...");

        mStorage = FirebaseStorage.getInstance();
        storagereference = mStorage.getReference("users").child(userID).child("outfits");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(userID).child("outfits");

        addToOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateOutfitActivity.this, ChooseElementsActivity.class));
            }
        });


        saveOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadIMages();
            }
        });

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });

    }

    private void UploadIMages() {

        // we need list that images urls
        for (int i = 0; i < uris.size(); i++) {
            Uri IndividualImage = uris.get(i);
            if (IndividualImage != null) {
                progressDialog.show();
                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ItemImages");
                final StorageReference ImageName = ImageFolder.child("Image" + i + ": " + IndividualImage.getLastPathSegment());
                ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlsList.add(String.valueOf(uri));
                                if (urlsList.size() == uris.size()) {
                                    StoreLinks(urlsList);
                                }
                            }
                        });

                    }
                });
            } else {
                Toast.makeText(this, "Please fill All Field", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void StoreLinks(ArrayList<String> urlsList) {
        // now we need get text from EditText
        String outfitNameTxt = outfitName.getText().toString();
        if (!TextUtils.isEmpty(outfitNameTxt) && imageUri != null) {
            // now we need a model class
            Outfit outfitData = new Outfit(outfitNameTxt, urlsList);

//            reference.child(outfitNameTxt).setValue(outfitData);
            String key = reference.push().getKey();
            reference.child(key).setValue(outfitData);
            progressDialog.dismiss();
            // if data uploaded successfully then show toast
            Toast.makeText(CreateOutfitActivity.this, "Your data Uploaded Successfully", Toast.LENGTH_SHORT).show();

        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please Fill All field", Toast.LENGTH_SHORT).show();
        }
        // if you want to clear viewpager after uploading Images
        uris.clear();

    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(CreateOutfitActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateOutfitActivity.this, new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            } else {
                PickImageFromGallery();
            }
        } else {
            PickImageFromGallery();
        }
    }

    private void PickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                imageUri = data.getClipData().getItemAt(i).getUri();
                                uris.add(imageUri);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            imageUri = data.getData();
                            uris.add(imageUri);
                        }
                        adapter.notifyDataSetChanged();
                    } else if (result.getData() != null) {
                        Toast.makeText(CreateOutfitActivity.this, "Изображения не были выбраны",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    public void clicked(int getSize) {

    }
}

