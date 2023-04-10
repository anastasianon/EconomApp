package com.example.ecommapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommapp.R;
import com.example.ecommapp.models.NewProductsModel;
import com.example.ecommapp.models.PopularProductsModel;
import com.example.ecommapp.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,description,price,quantity;
    Button addToCard,buyNow;
    ImageView addItems,removeItems;

    Toolbar toolbar;

    int totalQuantity = 1;
    int totalPrice = 0;


    //New Products модели
    NewProductsModel newProductsModel = null;

    //Popular Products модели
    PopularProductsModel popularProductsModel = null;

    //Show all модели
    ShowAllModel showAllModel = null;

    private FirebaseFirestore firestore;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        //Стрелочка при нажатии на которую происходит возврат на окно назад(появление)
        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Нажатие на эту кнопку кликабельность
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // передает detailed в переменную obj из другой активности
        final Object obj = getIntent().getSerializableExtra("detailed");

        // проверка, был ли объект, на который ссылается переменная obj, создан на основе какого-либо класса NewProductsModel
        if(obj instanceof NewProductsModel){
            newProductsModel = (NewProductsModel) obj;
        }else if (obj instanceof PopularProductsModel){
            popularProductsModel = (PopularProductsModel) obj;
        }else if (obj instanceof ShowAllModel){
            showAllModel = (ShowAllModel) obj;
        }

        //присваевание перменых из ксмл айдишник выбираю
        detailedImg = findViewById(R.id.detailed_img);
        quantity = findViewById(R.id.quantity);
        rating = findViewById(R.id.rating);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);

        addToCard = findViewById(R.id.add_to_card);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //В окне подробной информации о товаре к этим переменным
        // присваиваются значения выбранного товара и отображаются в окне
        //New Products
        if(newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());

            totalPrice = newProductsModel.getPrice() * totalQuantity;
        }

        //Popular Products
        if(popularProductsModel != null){
            Glide.with(getApplicationContext()).load(popularProductsModel.getImg_url()).into(detailedImg);
            name.setText(popularProductsModel.getName());
            rating.setText(popularProductsModel.getRating());
            description.setText(popularProductsModel.getDescription());
            price.setText(String.valueOf(popularProductsModel.getPrice()));
            name.setText(popularProductsModel.getName());

            totalPrice = popularProductsModel.getPrice() * totalQuantity;
        }

        //Show All
        if(showAllModel != null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;
        }

        //Кнопка купить сейчас
        // intent.putExtra переносит значение которое хранится в модели в другую активность AddressActivity
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // переход в окно AddressActivity
                Intent intent = new Intent(DetailedActivity.this, AddressActivity.class);

                if(newProductsModel != null){
                    intent.putExtra("item",newProductsModel);
                }
                if(popularProductsModel != null){
                    intent.putExtra("item",popularProductsModel);
                }
                if(showAllModel != null){
                    intent.putExtra("item",showAllModel);
                }
                startActivity(intent);
            }
        });

        //Кнопка добавление в корзину
        addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoCard();
            }
        });

        //Добавление колчиества товара (на плюс)
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Колчество меньше 10, то общее количество прибалвяется на 1,
                // и меняется значение в TextView quantity на количество товара
                // После стоимость умножается на количство и получается итоговая сумма
                if(totalQuantity < 10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(newProductsModel != null){

                        totalPrice = newProductsModel.getPrice() * totalQuantity;
                    }
                    if(popularProductsModel != null){

                        totalPrice = popularProductsModel.getPrice() * totalQuantity;
                    }
                    if(showAllModel != null){

                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }
            }
        });

        //Убавление колчиества товара (на минус)
        // textview quantity присваевается значение количества в string
        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

    }

    // метод добавление в корзину
    private void addtoCard() {
        // Добавление текущей даты и времени в переменные типа String
        String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        //происходит добавление в hashmap переменных, после чего
        // создается коллеция в firestore для конеретной корзины
        // текущего пользователя и записываются туда данные
        //Высвечивается сообщение "Добавлено в корзину"
        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Добавлено в корзину", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}