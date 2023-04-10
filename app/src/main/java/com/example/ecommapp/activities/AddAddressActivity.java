package com.example.ecommapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    EditText name, address, city, postalCode, phoneNumber;
    Toolbar toolbar;

    Button addAddressBtn;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

      //Стрелочка при нажатии на которую происходит возврат на окно назад(появление)
        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         //Нажатие на эту кнопку кликабельность
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //присваевание перменых из ксмл айдишник выбираю
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        name = findViewById(R.id.ad_name);
        address = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        postalCode = findViewById(R.id.ad_code);
        phoneNumber = findViewById(R.id.ad_phone);
        addAddressBtn = findViewById(R.id.ad_add_address);

        //какое дейтсиве будет производиться нажатиием на кнопку
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            //объявл перменных и присваение им начений который пользователь напишет в эдит текстах
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userCity = city.getText().toString();
                String userAddress = address.getText().toString();
                String userCode = postalCode.getText().toString();
                String userNumber = phoneNumber.getText().toString();

                // при вводе данных в эдит текст если они не нулевые, то они добавлюся в финальный адрес
                String final_address = "";

                if(!userName.isEmpty()){
                    final_address+=userName + ", ";
                }
                if(!userCity.isEmpty()){
                    final_address+=userCity + ", ";
                }
                if(!userAddress.isEmpty()){
                    final_address+=userAddress + ", ";
                }
                if(!userCode.isEmpty()){
                    final_address+=userCode + ", ";
                }
                if(!userNumber.isEmpty()){
                    final_address+=userNumber + ", ";
                }
                //если все эдит тексты не нулевые происходит добавление в hashmap переменной final_address, после чего
                // создается коллеция в firestore для пользователя под которым авторизирован и в коллекцию Address добавляется переменная final_address
                // После происходит переход на предыдущую страницу
                if(!userName.isEmpty() && !userCity.isEmpty() && !userAddress.isEmpty() && !userCode.isEmpty() && !userNumber.isEmpty()){

                    Map<String,String> map = new HashMap<>();
                    map.put("userAddress", final_address);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddAddressActivity.this, "Добавлен адрес", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddAddressActivity.this,AddressActivity.class));
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(AddAddressActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}