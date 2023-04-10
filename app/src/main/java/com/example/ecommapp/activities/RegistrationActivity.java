package com.example.ecommapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, password;

    private FirebaseAuth auth;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        //Проверка если пользователь зарегистрирован то перейти на активность Главное окно
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }

        //присваевание перменых из ксмл айдишник выбираю
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        //Для открытия стартового окна с анимацией один раз при запуске приложения
        sharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);

        boolean isFirstTime = sharedPreferences.getBoolean("firstTime", true);

        if(isFirstTime){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();

            Intent intent = new Intent(RegistrationActivity.this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //метод регистрации пользователя
    public void signup(View view) {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        //проверка на заполнение всех полей
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Введите Имя", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Введите адрес электронной почты!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Введите пароль!", Toast.LENGTH_SHORT).show();
            return;
        }
        //проверка на длину пароля
        if(userPassword.length() < 6){
            Toast.makeText(this, "Пароль слишком короткий, введите минимум 6 символов!", Toast.LENGTH_SHORT).show();
            return;
        }

        //создание пользователя в firebase Auth при помощи почты и пароля
        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Вывод сообщения об успешно регистрации и переход на главное окно
                            Toast.makeText(RegistrationActivity.this, "Успешно зарегистрирован", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));

                        }else
                        {
                            //Вывод сообщения если регистрация не удалась
                            Toast.makeText(RegistrationActivity.this, "Регистрация не удалась!" + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    //Кнопка для перехода на окно авторизации
    public void signin(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

    }
}