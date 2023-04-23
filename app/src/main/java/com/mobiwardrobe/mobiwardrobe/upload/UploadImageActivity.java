package com.mobiwardrobe.mobiwardrobe.upload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.mobiwardrobe.mobiwardrobe.MainActivity;
import com.mobiwardrobe.mobiwardrobe.R;

public class UploadImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    
    private EditText imageName;
    private ImageView imageView;
    private ProgressBar progressBar;
    private EditText type;
    private EditText color;
    private EditText season;
    private EditText weather;
    ImageButton buttonChooseImage;
    ImageButton buttonChoosePhoto;
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
        buttonChoosePhoto = findViewById(R.id.bt_choose_photo);
        buttonUpload = findViewById(R.id.bt_upload);
        buttonShowUploads = findViewById(R.id.bt_show_uploads);
        imageName = findViewById(R.id.et_image_name);
        imageView = findViewById(R.id.iv_for_upload);
        progressBar = findViewById(R.id.pb_image_upload);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        storageReference = FirebaseStorage.getInstance().getReference("users").child(userID).child("clothes");
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("clothes");

        type = findViewById(R.id.et_type);
        color = findViewById(R.id.et_color);
        season = findViewById(R.id.et_season);
        weather = findViewById(R.id.et_weather);

//        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                startActivityForResult(open_camera, CAMERA_REQUEST_CODE);
//                CropImage.activity().start(UploadImageActivity.this);
//            }
//        });

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        imageView.setImageURI(imageUri);
                    } else {
                        Toast.makeText(UploadImageActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//            if (resultCode == RESULT_OK) {
//                imageUri = result.getUri();
//                imageView.setImageURI(imageUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Toast.makeText(this, "Possible error: " + error, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String imageNameTxt = imageName.getText().toString();
            String typeTxt = type.getText().toString().trim();
            String colorTxt =  color.getText().toString().trim();
            String seasonTxt = season.getText().toString().trim();
            String weatherTxt = weather.getText().toString().trim();


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