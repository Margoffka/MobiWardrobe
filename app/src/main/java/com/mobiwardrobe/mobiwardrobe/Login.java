package com.mobiwardrobe.mobiwardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mobiwardrobe-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.et_email);
        final EditText password = findViewById(R.id.et_password);
        final Button loginBtn = findViewById(R.id.bt_login);
        final TextView registerNowBtn = findViewById(R.id.bt_register_now);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(Login.this, "Остутсвует логин или пароль", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Вход успешно выполнен", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
//                            finish();
                        }else{
                            Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//                        //check if email exist
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.hasChild(emailTxt)){
//                                final String getPassword = snapshot.child(emailTxt).child("password").getValue(String.class);
//
//                                assert getPassword != null;
//                                if (getPassword.equals(passwordTxt)){
//                                    Toast.makeText(Login.this, "Вход успешно выполнен", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(Login.this, MainActivity.class));
//                                    finish();
//                                } else {
//                                    Toast.makeText(Login.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            else {
//                                Toast.makeText(Login.this, "Такого пользователя не существует", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });

                }
            }
        });



        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Register activity
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }
}