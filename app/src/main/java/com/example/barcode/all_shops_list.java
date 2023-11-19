package com.example.barcode;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class all_shops_list extends AppCompatActivity {
    private RecyclerView ReView;
    private adpter_shops adpter_shops;
    private List<shops> shopsList;
    private ImageView all_shops_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shops_list);
        all_shops_exit=findViewById(R.id.add_shops_exit);
        ReView = findViewById(R.id.recycleview55);
        shopsList=test();
        adpter_shops=new adpter_shops(getBaseContext(),shopsList);
        ReView.setAdapter(adpter_shops);
        ReView.setLayoutManager(new LinearLayoutManager(all_shops_list.this));

        all_shops_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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