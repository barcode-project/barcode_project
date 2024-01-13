package com.example.barcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barcode.Adapter.adpter_shops;
import com.example.barcode.Items.shops;
import com.example.barcode.Server.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddedOrgsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private adpter_shops adpterShops;
    private List<shops> shopsList;
    private ImageView allShopsExit;
    private SearchView searchView;
    private SharedPreferences sharedPreferences;
    private ContentLoadingProgressBar progressBar;
    private LinearLayout noOrdersLayout;
    private TextView textError;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_orgs_list);

        allShopsExit = findViewById(R.id.all_shops_exit);
        recyclerView = findViewById(R.id.recycleview55);
        noOrdersLayout = findViewById(R.id.no_orders_layout);
        textError = findViewById(R.id.texterror);
        progressBar = findViewById(R.id.a_s_progressBar);
        swipeRefreshLayout = findViewById(R.id.swipe);

        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        initializeUI();
        allShopsExit.setOnClickListener(view -> finish());

        searchView = findViewById(R.id.txt_search);
        setupSearchListener();

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            fetchData();
        }, 2000));

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );
    }

    private void initializeUI() {
        fetchData();
    }

    private void setupSearchListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                return true;
            }
        });
    }

    private void filterResults(String text) {
        List<shops> filteredList = new ArrayList<>();
        if (shopsList != null) {
            for (shops shop : shopsList) {
                if (shop.getName_shop().toLowerCase().startsWith(text.toLowerCase()) ||
                        shop.getOwner_name().toLowerCase().startsWith(text.toLowerCase()) ||
                        shop.getStatus().toLowerCase().startsWith(text.toLowerCase())) {
                    filteredList.add(shop);
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, " لا يوجد منشئة بهذا الاسم", Toast.LENGTH_LONG).show();
            } else {
                if (adpterShops != null) {
                    adpterShops.setFlteredList(filteredList);
                } else {
                    Log.e("Adapter Error", "adpterShops is null");
                }
            }
        } else {
            Log.e("List Error", "shopsList is null");
            Toast.makeText(this, "قائمة المحلات فارغة", Toast.LENGTH_LONG).show();
        }
    }

    private void fetchData() {
        noOrdersLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, URLs.GET_VIR_ORGS,
                this::handleSuccessResponse,
                this::handleErrorResponse
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("auth-token", token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void handleSuccessResponse(String response) {
        List<shops> shops = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (object.getBoolean("success")) {
                JSONArray array = new JSONArray(object.getString("data"));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject citizen = array.getJSONObject(i);
                    shops shop = new shops();
                    shop.setId(citizen.getInt("id"));
                    shop.setName_shop(citizen.getString("org_name"));
                    shop.setStatus(String.valueOf(citizen.getInt("is_moved")));
                    shop.setOwner_name(citizen.getString("owner_name"));
                    shop.setOwner_namefullname(citizen.getString("user_name"));

                    shops.add(shop);
                    Log.d("ALL_SHOP", String.valueOf(citizen));
                }
                shopsList = shops;
                updateUI(shops);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            handleErrorResponse(new VolleyError(e.getMessage()));
        }
        progressBar.setVisibility(View.GONE);
    }

    private void handleErrorResponse(VolleyError error) {
        String errorMessage = "خطأ غير معروف";
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            errorMessage = "فشل الاتصال بالخادم. يرجى التحقق من اتصال الإنترنت والمحاولة مرة أخرى.";
        } else if (error instanceof AuthFailureError) {
            errorMessage = "فشل التحقق من الهوية. يرجى إعادة تسجيل الدخول.";
        } else if (error instanceof ServerError) {
            errorMessage = "حدث خطأ في الخادم. يرجى المحاولة مرة أخرى في وقت لاحق.";
        } else if (error instanceof NetworkError) {
            errorMessage = "فشل الاتصال بالخادم. يرجى التحقق من اتصال الإنترنت والمحاولة مرة أخرى.";
        } else if (error instanceof ParseError) {
            errorMessage = "حدث خطأ أثناء معالجة البيانات. يرجى المحاولة مرة أخرى في وقت لاحق.";
        } else if (error instanceof ServerError && error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            if (statusCode == 400) {
                errorMessage = "خطأ في الطلب: تحقق من البيانات المرسلة.";
            } else if (statusCode == 401) {
                errorMessage = "غير مصرح.";
            } else if (statusCode == 404) {
                errorMessage = "المورد غير موجود.";
            } else if (statusCode == 443) {
                errorMessage = "خطاء في الشهادة الامان.";
            }
        }
        Toast.makeText(AddedOrgsList.this, errorMessage, Toast.LENGTH_SHORT).show();
        textError.setText(errorMessage);
        noOrdersLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

    }

    private void updateUI(List<shops> shops) {
        if (shops.isEmpty()) {
            noOrdersLayout.setVisibility(View.VISIBLE);
            textError.setText("لا توجد بيانات");
        } else {
            adpterShops = new adpter_shops(AddedOrgsList.this, shops, 2);
            recyclerView.setLayoutManager(new LinearLayoutManager(AddedOrgsList.this));
            recyclerView.setAdapter(adpterShops);
        }
    }
}
