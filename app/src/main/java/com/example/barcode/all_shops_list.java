package com.example.barcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barcode.Server.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class all_shops_list extends AppCompatActivity {
    private RecyclerView ReView;
    private adpter_shops adpter_shops;
    private List<shops> shopsList;
    private ImageView all_shops_exit;
    private SearchView searchView;
    private SharedPreferences sharedPreferences;
    private LinearLayout liner;
    private TextView texterror;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shops_list);
        all_shops_exit = findViewById(R.id.all_shops_exit);
        ReView = findViewById(R.id.recycleview55);
        liner = findViewById(R.id.no_orders_layout);
        texterror=findViewById(R.id.texterror);
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
//
        shopsList = test();

        all_shops_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchView = findViewById(R.id.txt_search);
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
                filterListener(newText);

                return true;
            }


        });
    }
    private void filterListener(String text) {
        List<shops> filterList = new ArrayList<>();
        for (shops shop : shopsList){
            if (shop.getName_shop().toLowerCase().contains(text.toLowerCase())){
                filterList.add(shop);
            }
        }
        if (filterList.isEmpty()){
            Toast.makeText(this," لا يوجد منشئة بهاذا الاسم",Toast.LENGTH_LONG).show();
        }
        else {
            adpter_shops.setFlteredList(filterList);
        }

    }


    private List<shops> test() {
        List<shops> shops = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URLs.Get_Orgs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ALL_SHOPS_RESPONSE", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")) {
                        JSONArray array = new JSONArray(object.getString("data"));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject citizen = array.getJSONObject(i);

                            shops user = new shops();
//                            user.setNo(i+1);
                            user.setId(citizen.getInt("id"));
                            user.setName_shop(citizen.getString("org_name"));


                            shops.add(user);

                        }
                        adpter_shops = new adpter_shops(all_shops_list.this, shops);
                        ReView.setLayoutManager(new LinearLayoutManager(all_shops_list.this));
                        ReView.setAdapter(adpter_shops);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(all_shops_list.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(all_shops_list.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                texterror.setText(error.getMessage());
                liner.setVisibility(View.VISIBLE);
            }

        }) {

            // provide token in header

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
//                map.put("Authorization","Bearer "+token);
                map.put("auth-token", token);
                return map;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

//        shops.add(new shops("سوبر",1));
//        shops.add(new shops("صيدلية " ,1));
//        shops.add(new shops("كافيه",1));
//        shops.add(new shops("سوبر",1));
//        shops.add(new shops("صيدلية ",1));
//        shops.add(new shops("كافيه",1));
//        shops.add(new shops("سوبر",1));
//        shops.add(new shops("صيدلية ",1));
//        shops.add(new shops("كافيه",1));

        return shops;
    }
}