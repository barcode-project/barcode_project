package com.example.barcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchOrgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchOrgFragment extends Fragment {
    private RecyclerView recyclerView;
    private com.example.barcode.Adapter.adpter_shops adpter_shops;
    private List<shops> shopsList;
    private ImageView searchExit;
    private SearchView searchView;
    ProgressDialog loading;
    private SharedPreferences sharedPreferences;
    private int myIntParameter;
    public SearchOrgFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SearchOrgFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static SearchOrgFragment newInstance(int param1, String param2) {
//        SearchOrgFragment fragment = new SearchOrgFragment();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static SearchOrgFragment newInstance(int myIntParameter) {
        SearchOrgFragment fragment = new SearchOrgFragment();
        Bundle args = new Bundle();
        args.putInt("myIntParameter", myIntParameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myIntParameter = getArguments().getInt("myIntParameter", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search_org, container, false);
        recyclerView= view.findViewById(R.id.recycle_search);
        searchView= view.findViewById(R.id.etxt_search_org);
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        shopsList=generateDummy();

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid()){
                    if(myIntParameter == 1){
                        search(searchView.getQuery().toString());
                    }
                    else if(myIntParameter == 2){
                        search2(searchView.getQuery().toString());
                    }
                }
            }
        });

//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adpter_shops=new adpter_shops(requireContext(),shopsList,1);
//        recyclerView.setAdapter(adpter_shops);
        return view;
    }
    private List<shops> generateDummy() {
        List<shops> shoplist =new ArrayList<>();
        shoplist.add(new shops("hhh","1","xx",1,"khjk"));
        return shoplist;
    }

    private void search(String query) {
        List<shops> shops = new ArrayList<>();
        loading = new ProgressDialog(requireContext());
        loading.setMessage("انتظر من فضلك. . .");
        loading.setCancelable(false);
        loading.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLs.Search_orgs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_jk", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")) {
                        JSONArray array = new JSONArray(object.getString("data"));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject citizen = array.getJSONObject(i);
                            shops user = new shops();
                            user.setId(citizen.getInt("id"));
                            user.setName_shop(citizen.getString("org_name"));
                            user.setStatus(citizen.getString("license_status"));
                            user.setOwner_name(citizen.getString("owner_name"));

                            shops.add(user);
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adpter_shops=new adpter_shops(requireContext(),shops,1);
                        recyclerView.setAdapter(adpter_shops);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage="خطاء غير معروف";
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
                } else if (error instanceof ServerError && error.networkResponse != null) {
                    // يمكنك محاولة استخدام رمز الحالة الخاص بالخطأ من الاستجابة هنا
                    int statusCode = error.networkResponse.statusCode;
                    if (statusCode == 400) {
                        errorMessage = "خطأ في الطلب: تحقق من البيانات المرسلة.";
                    } else if (statusCode == 401) {
                        errorMessage = "غير مصرح.";
                    } else if (statusCode == 404) {
                        errorMessage = "المورد غير موجود.";
                    }else if (statusCode == 443) {
                        errorMessage = "خطاء في الشهادة الامان.";
                    }
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }

        }) {

            // provide token in header

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("auth-token", token);
                return map;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("search_value",query);

                return map;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void search2(String query) {
        List<shops> shops = new ArrayList<>();
        loading = new ProgressDialog(requireContext());
        loading.setMessage("انتظر من فضلك. . .");
        loading.setCancelable(false);
        loading.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLs.Search_vir_orgs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_jk", response);
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
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adpter_shops=new adpter_shops(requireContext(),shops,2);
                        recyclerView.setAdapter(adpter_shops);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage="خطاء غير معروف";
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
                } else if (error instanceof ServerError && error.networkResponse != null) {
                    // يمكنك محاولة استخدام رمز الحالة الخاص بالخطأ من الاستجابة هنا
                    int statusCode = error.networkResponse.statusCode;
                    if (statusCode == 400) {
                        errorMessage = "خطأ في الطلب: تحقق من البيانات المرسلة.";
                    } else if (statusCode == 401) {
                        errorMessage = "غير مصرح.";
                    } else if (statusCode == 404) {
                        errorMessage = "المورد غير موجود.";
                    }else if (statusCode == 443) {
                        errorMessage = "خطاء في الشهادة الامان.";
                    }
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }

        }) {

            // provide token in header

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("auth-token", token);
                return map;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("search_value",query);

                return map;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }


    private boolean valid() {
        if(searchView.getQuery().toString().isEmpty()){
            return false;
        }
        return true;
    }
}