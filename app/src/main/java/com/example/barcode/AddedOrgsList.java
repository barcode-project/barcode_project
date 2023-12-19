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
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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

public class AddedOrgsList extends AppCompatActivity {
    private RecyclerView ReView;
    private adpter_shops adpter_shops;
    private List<shops> shopsList;
    private ImageView all_shops_exit;
    private SearchView searchView;
    private SharedPreferences sharedPreferences;
    private ContentLoadingProgressBar progressBar;
    private LinearLayout liner;
    private TextView texterror;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_orgs_list);
        all_shops_exit = findViewById(R.id.all_shops_exit);
        ReView = findViewById(R.id.recycleview55);
        liner = findViewById(R.id.no_orders_layout);
        texterror=findViewById(R.id.texterror);
        progressBar=findViewById(R.id.a_s_progressBar);
        swipeRefreshLayout = findViewById(R.id.swipe);

        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        test();
        all_shops_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchView = findViewById(R.id.txt_search);

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
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                test();
            },  3000);
        });

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );

    }
    private void filterListener(String text) {
        List<shops> filterList = new ArrayList<>();
        if (shopsList != null) {
            for (shops shop : shopsList) {
                if (shop.getName_shop().toLowerCase().startsWith(text.toLowerCase())|| shop.getOwner_name().toLowerCase().startsWith(text.toLowerCase()) || shop.getStatus().toLowerCase().startsWith(text.toLowerCase())) {
                    filterList.add(shop);
                }
            }

            if (filterList.isEmpty()) {
                Toast.makeText(this, " لا يوجد منشئة بهذا الاسم", Toast.LENGTH_LONG).show();
            } else {
                if (adpter_shops != null) {
                    adpter_shops.setFlteredList(filterList);
                } else {
                    Log.e("Adapter Error", "adpter_shops is null");
                }
            }
        } else {
            Log.e("List Error", "shopsList is null");
            Toast.makeText(this, "قائمة المحلات فارغة", Toast.LENGTH_LONG).show();
        }


    }


    private List<shops> test() {
        progressBar.setVisibility(View.VISIBLE);
        List<shops> shops = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, URLs.GET_VIR_ORGS, new Response.Listener<String>() {
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
                            user.setStatus(citizen.getString("license_status"));
                            user.setOwner_name(citizen.getString("owner_name"));

                            shops.add(user);
                            Log.d("ALL_SHOP", String.valueOf(citizen));
                        }
                        shopsList = shops;
                        if (shopsList.isEmpty()) {
                            liner.setVisibility(View.VISIBLE);
                            texterror.setText("لا توجد بيانات");
                        } else {

                            adpter_shops = new adpter_shops(AddedOrgsList.this, shops, 2);
                            ReView.setLayoutManager(new LinearLayoutManager(AddedOrgsList.this));
                            ReView.setAdapter(adpter_shops);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddedOrgsList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    liner.setVisibility(View.VISIBLE);
                    texterror.setText(e.getMessage());
                }
                progressBar.setVisibility(View.GONE);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage="??";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            errorMessage = "فشل الاتصال بالخادم. يرجى التحقق من اتصال الإنترنت والمحاولة مرة أخرى.";
                            // handle time out error or no connection error
                        } else if (error instanceof AuthFailureError) {
                            errorMessage = "فشل التحقق من الهوية. يرجى إعادة تسجيل الدخول.";
                            // handle authentication failure error
                        } else if (error instanceof ServerError) {
                            errorMessage = "حدث خطأ في الخادم. يرجى المحاولة مرة أخرى في وقت لاحق.";
                            // handle server error
                        } else if (error instanceof NetworkError) {
                            errorMessage = "فشل الاتصال بالخادم. يرجى التحقق من اتصال الإنترنت والمحاولة مرة أخرى.";
                            // handle network error
                        } else if (error instanceof ParseError) {
                            errorMessage = "حدث خطأ أثناء معالجة البيانات. يرجى المحاولة مرة أخرى في وقت لاحق.";
                            // handle JSON parsing error
                        } else if (error instanceof ServerError && error.networkResponse != null) {
                            // يمكنك محاولة استخدام رمز الحالة الخاص بالخطأ من الاستجابة هنا
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
                            // يمكنك إضافة المزيد من الحالات حسب احتياجاتك
                        }
                        Toast.makeText(AddedOrgsList.this, errorMessage, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        texterror.setText(errorMessage);
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
        return shops;
    }
}