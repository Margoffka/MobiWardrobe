package com.mobiwardrobe.mobiwardrobe.upload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mobiwardrobe.mobiwardrobe.CustomSpinnerAdapter;
import com.mobiwardrobe.mobiwardrobe.MainActivity;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.model.Upload;

import java.util.Arrays;

public class UploadImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    
    private EditText imageName;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Spinner type;
    private Spinner color;
    private Spinner season;
    private Spinner weather;
    ImageButton buttonChooseImage;
    Button buttonUpload;
    ImageButton buttonShowUploads;

    private Uri imageUri;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String userID;

    private StorageTask uploadTask;

    //ActivityResultLauncher<Intent> activityResultLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        buttonChooseImage = findViewById(R.id.bt_choose_image);
        buttonUpload = findViewById(R.id.bt_upload);
        buttonShowUploads = findViewById(R.id.bt_show_uploads);
        imageName = findViewById(R.id.et_image_name);
        imageView = findViewById(R.id.iv_for_upload);
        progressBar = findViewById(R.id.pb_image_upload);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        storageReference = FirebaseStorage.getInstance().getReference("users").child(userID).child("clothes");
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("clothes");

        type = findViewById(R.id.sp_type);
        color = findViewById(R.id.sp_color);
        season = findViewById(R.id.sp_season);
        weather = findViewById(R.id.sp_weather);


        int textColor = getResources().getColor(R.color.my_purple); // Замените на ваш желаемый цвет
        CustomSpinnerAdapter weatherAdapter = new CustomSpinnerAdapter(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.weather_options)),
                textColor);
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weather.setAdapter(weatherAdapter);

        CustomSpinnerAdapter typeAdapter = new CustomSpinnerAdapter(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.clothes_types)),
                textColor);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);


        CustomSpinnerAdapter colorAdapter = new CustomSpinnerAdapter(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.clothes_colors)),
                textColor);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color.setAdapter(colorAdapter);

        CustomSpinnerAdapter seasonAdapter = new CustomSpinnerAdapter(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.clothes_seasons)),
                textColor);
        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        season.setAdapter(seasonAdapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(UploadImageActivity.this, "Upload in Progress",
                            Toast.LENGTH_SHORT).show();
                } else {
                    uploadImageToFirebase();
                }
            }
        });

        buttonShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClothesFragment();
            }
        });

    }

    private void openImageChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Добавьте изображение");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        activityResultLauncher.launch(chooserIntent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            imageView.setImageURI(imageUri);
                        } else {
                            Toast.makeText(UploadImageActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String imageNameTxt = imageName.getText().toString();
            String typeTxt = type.getSelectedItem().toString().trim();
            String colorTxt =  color.getSelectedItem().toString().trim();
            String seasonTxt = season.getSelectedItem().toString().trim();
            String weatherTxt = weather.getSelectedItem().toString().trim();


            final StorageReference imageReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
//            Log.d("TAG", mImageUri.toString());
            uploadTask = imageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uriTxt = uri.toString();
                                    //creating the upload object to store uploaded image details
                                    Upload upload = new Upload(imageNameTxt, colorTxt, seasonTxt,
                                            weatherTxt, typeTxt, uriTxt);
                                    String key = databaseReference.push().getKey();
                                    databaseReference.child(key).setValue(upload);
                                    startActivity(new Intent(UploadImageActivity.this, MainActivity.class));
                                    Toast.makeText(UploadImageActivity.this, "Успешно загружено", Toast.LENGTH_LONG).show();
                                }
                            });
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void openClothesFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}