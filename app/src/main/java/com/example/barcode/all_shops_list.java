package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class all_shops_list extends AppCompatActivity {
    private RecyclerView ReView;
    private adpter_shops adpter_shops;
    private List<shops> shopsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shops_list);
        ReView = findViewById(R.id.recycleview55);
        shopsList=test();
        adpter_shops=new adpter_shops(getBaseContext(),shopsList);
        ReView.setAdapter(adpter_shops);
        ReView.setLayoutManager(new LinearLayoutManager(all_shops_list.this));



    }
    private List<shops> test(){
        List shops=new ArrayList<>();
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