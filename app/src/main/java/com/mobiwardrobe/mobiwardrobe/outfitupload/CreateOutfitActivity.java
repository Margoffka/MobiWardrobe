package com.mobiwardrobe.mobiwardrobe.outfitupload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.adapters.CreateOutfitAdapter;
import com.mobiwardrobe.mobiwardrobe.outfit.Outfit;

import java.util.ArrayList;

public class CreateOutfitActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CreateOutfitAdapter adapter;

    EditText outfitName;
    Button saveOutfit;
    Button addToOutfit;

    ArrayList<Uri> uris;
    ArrayList<String> urlsList;

    FirebaseDatabase database;
    DatabaseReference reference;

    ProgressDialog progressDialog;

    private FirebaseUser firebaseUser;
    private String userID;

    ArrayList<String> arrayUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_outfit);

        uris = new ArrayList<>();
        urlsList = new ArrayList<>();

        setRecyclerView();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        outfitName = findViewById(R.id.et_outfit_name_upload);
        saveOutfit = findViewById(R.id.bt_outfit_save);
        addToOutfit = findViewById(R.id.bt_outfit_add);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка данных");
        progressDialog.setMessage("Подождите, пока комплект сохранится");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(userID).child("outfits");
        getImages();

        addToOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateOutfitActivity.this, ChooseElementsActivity.class));
                finish();
            }
        });

        saveOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadOutfit();
            }
        });
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.rv_show_elements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        adapter = new CreateOutfitAdapter(this, uris);
        recyclerView.setAdapter(adapter);
    }


    private void getImages() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            arrayUrls = bundle.getStringArrayList("ArrayUrls");
            for (int i = 0; i < arrayUrls.size(); i++) {
                Uri newUri = Uri.parse(arrayUrls.get(i));
                System.out.println(newUri);
                uris.add(newUri);
            }

        }

    }

    private void UploadOutfit() {
        for (int i = 0; i < uris.size(); i++) {
            progressDialog.show();
            urlsList.add(String.valueOf(uris.get(i)));
            if (urlsList.size() == uris.size()) {
                String outfitNameTxt = outfitName.getText().toString();
                if (!TextUtils.isEmpty(outfitNameTxt) && uris != null) {
                    Outfit outfitData = new Outfit(outfitNameTxt, urlsList);

                    String key = reference.push().getKey();
                    reference.child(key).setValue(outfitData);
                    progressDialog.dismiss();
                    uris.clear();

                    Toast.makeText(CreateOutfitActivity.this, "Комплект добавлен!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

