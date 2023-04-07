package com.mobiwardrobe.mobiwardrobe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class ClothesEditActivity extends AppCompatActivity {

    ImageView editImage;
    Button updateButton;
    EditText editName, editType, editColor, editSeason, editWeather;
    String name, type, color, season, weather;
    String imageUrl;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_edit);

        updateButton = findViewById(R.id.bt_edit_update);
        editColor = findViewById(R.id.et_edit_color);
        editType = findViewById(R.id.et_edit_type);
        editSeason = findViewById(R.id.et_edit_season);
        editWeather = findViewById(R.id.et_edit_weather);
        editName = findViewById(R.id.et_edit_name);
        editImage = findViewById(R.id.iv_for_edit);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            editImage.setImageURI(uri);
                        } else {
                            Toast.makeText(ClothesEditActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(ClothesEditActivity.this).load(bundle.getString("Image")).into(editImage);
            editName.setText(bundle.getString("Name"));
            editType.setText(bundle.getString("Type"));
            editColor.setText(bundle.getString("Color"));
            editSeason.setText(bundle.getString("Season"));
            editWeather.setText(bundle.getString("Weather"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials").child(key);
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
//                saveData();
                Intent intent = new Intent(ClothesEditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
//    public void saveData(){
//        storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());
//        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!uriTask.isComplete());
//                Uri urlImage = uriTask.getResult();
//                imageUrl = urlImage.toString();
//                updateData();
//                dialog.dismiss();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                dialog.dismiss();
//            }
//        });
//    }
//    public void updateData(){
//        title = updateTitle.getText().toString().trim();
//        desc = updateDesc.getText().toString().trim();
//        lang = updateLang.getText().toString();
//        DataClass dataClass = new DataClass(title, desc, lang, imageUrl);
//        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
//                    reference.delete();
//                    Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
