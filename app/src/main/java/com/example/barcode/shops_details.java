package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class shops_details extends AppCompatActivity {
    private ImageView bt_exit;
    private TextInputEditText id_no,last_licens,owner_name,shop_name,phone_no,activity_type,neighbor_unit,address_unit;
    private TextView front_signboard,side_signboard,elec_signboard,supetficail_signboard,stuck_signboard,mural_signborard,totl_price;
    private Button printBtn;
    private ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_details);
        progressBar=findViewById(R.id.all_details_progressBar);
        bt_exit=findViewById(R.id.exit);
        id_no=findViewById(R.id.insitiution_no);
        last_licens=findViewById(R.id.last_license);
        owner_name=findViewById(R.id.owner_name);
        shop_name=findViewById(R.id.shop_name);
        phone_no=findViewById(R.id.phone_no);
        activity_type=findViewById(R.id.activity_type);
        neighbor_unit=findViewById(R.id.neighbor_unit);
        address_unit=findViewById(R.id.address_unit);
        front_signboard=findViewById(R.id.front_signboard);
        side_signboard=findViewById(R.id.side_signboard);
        elec_signboard=findViewById(R.id.elec_signboard);
        supetficail_signboard=findViewById(R.id.supetficail_signboard);
        stuck_signboard=findViewById(R.id.stuck_signboard);
        mural_signborard=findViewById(R.id.mural_signborard);
        totl_price=findViewById(R.id.totl_price);
        printBtn=findViewById(R.id.printBtn);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });






    }
}