package com.example.barcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.textfield.TextInputEditText;

public class org_details extends AppCompatActivity {
    TextInputEditText address_unit, doors_numbers, owner_name, shop_name, phone_no, shop_type, note, signboard1, signboard2, signboard3, board_size_3, board_size_2, board_size_1;
    Button uploadButton;
    ContentLoadingProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    ViewPager viewPager;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_details);
//        pickimagebtn = findViewById(R.id.chooseImage);
        viewPager = findViewById(R.id.viewPager1);
        ImageView add_shops_exit = findViewById(R.id.orgdetails_exit);
        uploadButton = findViewById(R.id.upload_bt1);
        address_unit = findViewById(R.id.address_unit1);
        doors_numbers = findViewById(R.id.doors_numbers1);
        owner_name = findViewById(R.id.owner_name1);
        shop_name = findViewById(R.id.shop_name1);
        shop_type = findViewById(R.id.shop_type1);
        phone_no = findViewById(R.id.phone_no1);
        note = findViewById(R.id.note1);
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        id = getIntent().getIntExtra("id",0);

          address_unit.setEnabled(false);
          doors_numbers.setEnabled(false);
          owner_name.setEnabled(false);
          shop_type.setEnabled(false);
          phone_no.setEnabled(false);
          note.setEnabled(false);

    }
}