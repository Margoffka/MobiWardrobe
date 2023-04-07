package com.mobiwardrobe.mobiwardrobe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobiwardrobe.mobiwardrobe.upload.Upload;

public class EditClothesActivity extends AppCompatActivity {

    ImageView editImage;
    Button updateButton;
    EditText editName, editType, editColor, editSeason, editWeather;
    String name, type, color, season, weather;
    String imageUrl;
    String key, oldImageURL;
    Uri uri;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    private FirebaseUser firebaseUser;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_edit);

        updateButton = findViewById(R.id.bt_edit_update);
        editColor = findViewById(R.id.et_edit_color);
        editName = findViewById(R.id.et_edit_name);
        editType = findViewById(R.id.et_edit_type);
        editSeason = findViewById(R.id.et_edit_season);
        editWeather = findViewById(R.id.et_edit_weather);
        editImage = findViewById(R.id.iv_for_edit);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            editImage.setImageURI(uri);
                        } else {
                            Toast.makeText(EditClothesActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(EditClothesActivity.this).load(bundle.getString("Image")).into(editImage);
            editName.setText(bundle.getString("Name"));
            editType.setText(bundle.getString("Type"));
            editColor.setText(bundle.getString("Color"));
            editSeason.setText(bundle.getString("Season"));
            editWeather.setText(bundle.getString("Weather"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID)
                .child("clothes").child(key);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(EditClothesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditClothesActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference("users").child(userID)
                    .child("clothes").child(System.currentTimeMillis() + "." + getFileExtension(uri));
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    updateData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        } else {
            imageUrl = oldImageURL;
            updateData();
        }
    }

    public void updateData() {
        name = editName.getText().toString().trim();
        type = editType.getText().toString().trim();
        color = editColor.getText().toString().trim();
        season = editSeason.getText().toString().trim();
        weather = editWeather.getText().toString().trim();
        Upload upload = new Upload(name, color, season, weather, type, imageUrl);
        databaseReference.setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(EditClothesActivity.this, "Обновлено", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditClothesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
