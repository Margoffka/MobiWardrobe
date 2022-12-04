package com.mobiwardrobe.mobiwardrobe;

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

public class Register extends AppCompatActivity {


    //Create object of DatabaseReference class to access firebase's Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mobiwardrobe-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText firstname = findViewById(R.id.et_firstname);
        final EditText email = findViewById(R.id.et_email);
        final EditText password = findViewById(R.id.et_password);
        final EditText confirmPassword = findViewById(R.id.et_confirm_password);

        final Button registerBtn = findViewById(R.id.bt_register);
        final TextView loginNowBtn = findViewById(R.id.bt_login_now);

        mAuth = FirebaseAuth.getInstance();


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                createUser();

                //get data from EditTexts into String variables
                final String firstnameTxt = firstname.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmPasswordTxt = confirmPassword.getText().toString();

                //check if user fill all the fields before sending data to firebase
                if (firstnameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()) {
                    Toast.makeText(Register.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }

                //check if passwords are matching with each other
                else if (!passwordTxt.equals(confirmPasswordTxt)) {
                    Toast.makeText(Register.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
//                    confirmPassword.requestFocus();
                } else {

                    mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(Register.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

//                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            //check if email is not registered before
//
//                            if (snapshot.hasChild(emailTxt)) {
//                                Toast.makeText(Register.this, "Такой пользователь уже существует", Toast.LENGTH_SHORT).show();
//                            } else {
//
//                                //sending data to firebase Realtime Database
////                                databaseReference.child("users").child(emailTxt).child("firstname").setValue(firstnameTxt);
////                                databaseReference.child("users").child(emailTxt).child("password").setValue(passwordTxt);
////
////                                Toast.makeText(Register.this, "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show();
////                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                }
            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//        private void createUser(){
//            String email = etRegEmail.getText().toString();
//            String password = etRegPassword.getText().toString();
//
//            if (TextUtils.isEmpty(email)){
//                etRegEmail.setError("Email cannot be empty");
//                etRegEmail.requestFocus();
//            }else if (TextUtils.isEmpty(password)){
//                etRegPassword.setError("Password cannot be empty");
//                etRegPassword.requestFocus();
//            }else{
//                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                        }else{
//                            Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }
}
