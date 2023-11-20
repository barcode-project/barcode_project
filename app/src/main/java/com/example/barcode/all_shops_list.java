package com.example.barcode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class all_shops_list extends AppCompatActivity {
    private RecyclerView ReView;
    private adpter_shops adpter_shops;
    private List<shops> shopsList;
    private ImageView all_shops_exit;
    private SearchView searchView;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shops_list);
        all_shops_exit=findViewById(R.id.all_shops_exit);
        ReView = findViewById(R.id.recycleview55);
        shopsList=test();
        adpter_shops=new adpter_shops(getBaseContext(),shopsList);
        ReView.setLayoutManager(new LinearLayoutManager(all_shops_list.this));
        ReView.setAdapter(adpter_shops);

        all_shops_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchView=findViewById(R.id.txt_search);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("qrcode")) {
            String qrcodeData = intent.getStringExtra("qrcode");

            // الآن يمكنك استخدام qrcodeData كمعلومات إضافية في النشاط الثاني، على سبيل المثال، قد تربطها بحقل نص أو تعرضها في مكان مناسب
            searchView.setQuery(qrcodeData, false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // يمكنك تنفيذ إجراءات عند الضغط على زر البحث
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // يمكنك تنفيذ إجراءات عند تغيير نص البحث
                return false;
            }
        });
    }

    private List<shops> test(){
        List<shops> shops=new ArrayList<>();
        shops.add(new shops("سوبر",1));
        shops.add(new shops("صيدلية " ,1));
        shops.add(new shops("كافيه",1));
        shops.add(new shops("سوبر",1));
        shops.add(new shops("صيدلية ",1));
        shops.add(new shops("كافيه",1));
        shops.add(new shops("سوبر",1));
        shops.add(new shops("صيدلية ",1));
        shops.add(new shops("كافيه",1));

        return shops;
    }
}