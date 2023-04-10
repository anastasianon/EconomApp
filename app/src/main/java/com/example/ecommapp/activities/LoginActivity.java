package com.example.ecommapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //объявление переменных и присваивание id из xml EditText
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    //Метод авторизации, Переменные userEmail и userPassword присваиваются значения из едиттестов
    public void signin(View view) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        //Проверка на пустые значения
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Введите адрес электронной почты!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Введите пароль!", Toast.LENGTH_SHORT).show();
            return;
        }
        // проверка на длину пароля
        if(userPassword.length() < 6){
            Toast.makeText(this, "Пароль слишком короткий, введите минимум 6 символов!", Toast.LENGTH_SHORT).show();
            return;
        }
        //метод аутинтификации в firebase при помощи почты и пароля
        auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Успешная авторизация", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, "Ошибка:"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void signup(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

    }
}