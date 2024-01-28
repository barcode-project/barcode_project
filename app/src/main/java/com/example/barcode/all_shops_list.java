package com.example.barcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

public class all_shops_list extends AppCompatActivity {
    private RecyclerView ReView;
    private com.example.barcode.Adapter.adpter_shops adpter_shops;
    private List<shops> shopsList;
    private ImageView all_shops_exit;
    private EditText searchView;
    private SharedPreferences sharedPreferences;
    private LinearLayout liner,progressBar;
    private TextView texterror;
    SwipeRefreshLayout swipeRefreshLayout;
//    private int currentPage = 1;
//    private int itemsPerPage = 10;
//    private boolean isLoading = false;
    private int currentPage = 1; // initial page
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shops_list);
        all_shops_exit = findViewById(R.id.all_shops_exit);
        ReView = findViewById(R.id.recycleview55);
        liner = findViewById(R.id.no_orders_layout);
        texterror=findViewById(R.id.texterror);
        progressBar=findViewById(R.id.a_s_progressBar);
        swipeRefreshLayout = findViewById(R.id.swipe);

        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);


        ReView.setLayoutManager(new LinearLayoutManager(this));
        shopsList = new ArrayList<>();
        adpter_shops = new adpter_shops(this, shopsList, 1);
        ReView.setAdapter(adpter_shops);

        ReView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    // Load more data
                    fetchData();
                }
            }
        });

        fetchData();

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
//            searchView.setQuery(qrcodeData, false);
        }
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // يمكنك تنفيذ إجراءات عند الضغط على زر البحث
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterListener(newText);
//
//                return true;
//            }
//
//
//        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSearchFragment();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() ->  new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
//                test();
            fetchData();
            },  2000));


        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );

    }
    private void loadSearchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create a new instance of the SearchOrgFragment and pass the int parameter
        SearchOrgFragment searchFragment = SearchOrgFragment.newInstance(1);

        // Replace the current content with the new fragment
        fragmentTransaction.replace(android.R.id.content, searchFragment);

        // Add the transaction to the back stack, allowing the user to navigate back
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction to apply the changes
        fragmentTransaction.commit();
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


    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        liner.setVisibility(View.GONE);

        String apiUrl = URLs.Get_Orgs_V2 + "?page=" + currentPage;

        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("SGDHH",response);
                    if (object.getBoolean("success")) {
                        JSONObject arr = object.getJSONObject("data");
                        JSONArray array = new JSONArray(arr.getString("data"));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject citizen = array.getJSONObject(i);

                            shops user = new shops();
                            user.setId(citizen.getInt("id"));
                            user.setName_shop(citizen.getString("org_name"));
                            user.setStatus(citizen.getString("license_status"));
                            user.setOwner_name(citizen.getString("owner_name"));

                            shopsList.add(user);
                            Log.d("ALL_SHOPSS", String.valueOf(citizen));
                        }

                        adpter_shops.notifyDataSetChanged(); // Notify adapter about data changes

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(all_shops_list.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    liner.setVisibility(View.VISIBLE);
                    texterror.setText(e.getMessage());
                } finally {
                    progressBar.setVisibility(View.GONE);
                    isLoading = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
                progressBar.setVisibility(View.GONE);
                liner.setVisibility(View.VISIBLE);
                isLoading = false;
            }
        }) {
            // provide token in header
            @Override
            public Map<String, String> getHeaders() {
                String token = sharedPreferences.getString("token", "");
                HashMap<String, String> headers = new HashMap<>();
                headers.put("auth-token", token);
                return headers;
            }
        };

        // Add the request to the queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        // Increment the current page for the next request
        currentPage++;
    }

    private void handleVolleyError(VolleyError error) {
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

        Toast.makeText(all_shops_list.this, errorMessage, Toast.LENGTH_SHORT).show();
        texterror.setText(errorMessage);
    }
}