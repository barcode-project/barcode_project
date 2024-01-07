package com.example.barcode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.viewpager.widget.ViewPager;

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
import com.example.barcode.Adapter.ViewPagerAdapter;
import com.example.barcode.Adapter.ViewPagerAdapter2;
import com.example.barcode.Items.shops;
import com.example.barcode.Server.URLs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddedShopDetails extends AppCompatActivity {
    private ImageView bt_exit;
    TextInputEditText id_no, last_licens, owner_name, shop_name, phone_no, activity_type, shownote, address_unit,type,quan;
    AppCompatButton show_billboard,add_btn,show_image,add_image;
    ContentLoadingProgressBar progressBar;
    Button upload_billboard_bt;
    private SharedPreferences sharedPreferences;
    private int id;
    ArrayAdapter<String> shopstypeAdapter;
    String selectedshopstypID,longitudelength,latitudewidth;
    List<HashMap<String, String>> shopsCategory;
    List<String> name_shops_type;
    ArrayList<String> ImageList;

    Spinner spinner;
    String selectedItem;
    private List<PlateData> Plate ;
    ImageView imageView;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_shop_details);
        progressBar=findViewById(R.id.all_details_progressBar);
        bt_exit=findViewById(R.id.shops_exit);
        id_no=findViewById(R.id.insitiution_no);
        last_licens=findViewById(R.id.last_license);
        owner_name=findViewById(R.id.owner_name);
        shop_name=findViewById(R.id.shop_name);
        phone_no=findViewById(R.id.phone_no);
        activity_type=findViewById(R.id.activity_type);
        shownote=findViewById(R.id.noteshow);
        address_unit=findViewById(R.id.address_unit);
        show_billboard = findViewById(R.id.show_billboard);
        add_btn = findViewById(R.id.add_btn);
        show_image = findViewById(R.id.show_image);
        add_image = findViewById(R.id.add_image);
        imageView = findViewById(R.id.imageView);
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        id = getIntent().getIntExtra("id",0);
        Log.d("ALL_ID", String.valueOf(id));
        ImageList = new ArrayList<>();
//        test();
        fetchDataFromServer();

        show_billboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog();
            }
        });


        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openInputActivity();
                Intent intent = new Intent(AddedShopDetails.this,AddBoard.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
        FloatingActionButton button=findViewById(R.id.fab1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double latitude = Double.parseDouble(latitudewidth); // خط العرض لموقع العميل
                    double longitude = Double.parseDouble(longitudelength); // خط الطول لموقع العميل
                    String label = "موقع العميل"; // اسم الموقع الذي سيظهر في خرائط جوجل

                    String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + label + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps"); // تحديد تطبيق خرائط جوجل

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }catch (Exception e)  {
                    e.printStackTrace();
                    Toast.makeText(AddedShopDetails.this,  "لاتوجد احداثيات\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
//        show_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddedShopDetails.this);
//                View dialogView = getLayoutInflater().inflate(R.layout.viewpagerimage, null);
//                dialogBuilder.setView(dialogView);
//
//                ViewPager dialogViewPager = dialogView.findViewById(R.id.viewPager);
//                ViewPagerAdapter2 adapter = new ViewPagerAdapter2(AddedShopDetails.this, chooseImageList);
//                dialogViewPager.setAdapter(adapter);
//
//                AlertDialog alertDialog = dialogBuilder.create();
//                alertDialog.show();
//
//            }
//        });
//        add_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AddedShopDetails.this,Add_Iamge.class);
//                intent.putExtra("id",id);
//                startActivity(intent);
//                finish();
//            }
//        });
    }


//    private void test() {
////        List<shops> shops = new ArrayList<>();
//        List<PlateData> list = new ArrayList<>();
//        StringRequest request = new StringRequest(Request.Method.POST, URLs.Get_Vir_Org, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("ALL_SHOPS_RESPONSE", response);
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if (object.getBoolean("success")) {
//                        JSONObject citizen = new JSONObject(object.getString("data"));
//                        owner_name.setText(citizen.getString("owner_name"));
//                        id_no.setText(String.valueOf(citizen.getInt("id")));
//                        shop_name.setText(citizen.getString("org_name"));
//                        phone_no.setText(citizen.getString("owner_phone"));
//                        address_unit.setText(citizen.getString("street_name"));
//                        activity_type.setText(citizen.getString("org_type_name"));
//                        shownote.setText(citizen.getString("note"));
//                        longitudelength=citizen.getString("log_y");
//                        latitudewidth=citizen.getString("log_x");
//                        if (citizen.getString("org_image").length() > 1) {
//                        chooseImageList.add(citizen.getString("org_image"));
//                        }
//                        JSONArray array = new JSONArray(citizen.getString("billboard"));
//                        for (int i = 0; i < array.length(); i++) {
//                        JSONObject billboard = array.getJSONObject(i);
//                        JSONObject type_billboard = billboard.getJSONObject("billboard");
//
//                            PlateData board = new PlateData();
////                            user.setNo(i+1);
//                            board.setPlateType(type_billboard.getString("name"));
//                            board.setLength(String.valueOf(billboard.getDouble("height")));
//                            board.setWidth(String.valueOf(billboard.getDouble("width")));
//                            board.setQuantity(String.valueOf(billboard.getInt("count")));
//
//                            list.add(board);
//
//                        }
//                        Plate = list;
//                        Log.d("ALL_SHOPS_RESP", String.valueOf(citizen));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(AddedShopDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                //                progressBar.setVisibility(View.GONE);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                String errorMessage="خطأ غير معروف";
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    errorMessage = "فشل الاتصال بالخادم. يرجى التحقق من اتصال الإنترنت والمحاولة مرة أخرى.";
//                    // handle time out error or no connection error
//                } else if (error instanceof AuthFailureError) {
//                    errorMessage = "فشل التحقق من الهوية. يرجى إعادة تسجيل الدخول.";
//                    // handle authentication failure error
//                } else if (error instanceof ServerError) {
//                    errorMessage = "حدث خطأ في الخادم. يرجى المحاولة مرة أخرى في وقت لاحق.";
//                    // handle server error
//                } else if (error instanceof NetworkError) {
//                    errorMessage = "فشل الاتصال بالخادم. يرجى التحقق من اتصال الإنترنت والمحاولة مرة أخرى.";
//                    // handle network error
//                } else if (error instanceof ParseError) {
//                    errorMessage = "حدث خطأ أثناء معالجة البيانات. يرجى المحاولة مرة أخرى في وقت لاحق.";
//                    // handle JSON parsing error
//                }
//                Toast.makeText(AddedShopDetails.this, errorMessage, Toast.LENGTH_SHORT).show();
//                //                progressBar.setVisibility(View.GONE);
//            }
//
//        }) {
//
//            // provide token in header
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                String token = sharedPreferences.getString("token", "");
//                HashMap<String, String> map = new HashMap<>();
//                map.put("auth-token", token);
//                return map;
//            }
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> map = new HashMap<>();
//                map.put("id",String.valueOf(id));
//                return map;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);
//
//    }

    private void fetchDataFromServer() {
        List<PlateData> plateList = new ArrayList<>();
        loading = new ProgressDialog(AddedShopDetails.this);
        loading.setMessage("انتظر من فضلك. . .");
        loading.setCancelable(false);
        loading.show();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URLs.Get_Vir_Org,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleApiResponse(response, plateList);
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleApiError(error);
                        loading.dismiss();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthHeaders();
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return createRequestParams();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void handleApiResponse(String response, List<PlateData> plateList) {
        try {
            JSONObject object = new JSONObject(response);
            if (object.getBoolean("success")) {
                JSONObject citizen = object.getJSONObject("data");

                updateUIFromCitizenData(citizen);

//                if (citizen.has("org_image") && citizen.getString("org_image").length() > 1) {
//                    chooseImageList.add(citizen.getString("org_image"));
//                }

//                if (citizen.has("org_image")) {
//                    String orgImage = citizen.optString("org_image");
//                    if (orgImage != null && orgImage.length() > 1) {
//                        chooseImageList.add(orgImage);
//                    }
//                }

                JSONArray array = citizen.getJSONArray("billboard");
                plateList.addAll(parseBillboardArray(array));

                Plate = plateList;
                Log.d("ALL_SHOPS_RESP", String.valueOf(citizen));
            }
        } catch (JSONException e) {
            handleJsonParsingError(e);
        }
    }

    private void updateUIFromCitizenData(JSONObject citizen) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                owner_name.setText(citizen.optString("owner_name"));
                id_no.setText(String.valueOf(citizen.optInt("id")));
                shop_name.setText(citizen.optString("org_name"));
                phone_no.setText(citizen.optString("owner_phone"));
                address_unit.setText(citizen.optString("street_name"));
                activity_type.setText(citizen.optString("org_type_name"));
                shownote.setText(citizen.optString("note"));
                longitudelength = citizen.optString("log_y");
                latitudewidth = citizen.optString("log_x");
                String fullImageUrl = "https://demo.qryemen.com/" + citizen.optString("org_image");

                // Use Picasso to load and display the image
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.get().load(fullImageUrl).into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private List<PlateData> parseBillboardArray(JSONArray array) throws JSONException {
        List<PlateData> plateList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject billboard = array.getJSONObject(i);
            JSONObject type_billboard = billboard.getJSONObject("billboard");

            PlateData board = new PlateData();
            board.setPlateType(type_billboard.optString("name"));
            board.setLength(String.valueOf(billboard.optDouble("height")));
            board.setWidth(String.valueOf(billboard.optDouble("width")));
            board.setQuantity(String.valueOf(billboard.optInt("count")));

            plateList.add(board);
        }
        return plateList;
    }

    private void handleJsonParsingError(JSONException e) {
        e.printStackTrace();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddedShopDetails.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleApiError(VolleyError error) {
        final String errorMessage = "خطأ غير معروف";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            final String errorData = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            Log.e("VolleyError", errorData);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddedShopDetails.this, "خطأ: " + errorData, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddedShopDetails.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private Map<String, String> createAuthHeaders() {
        String token = sharedPreferences.getString("token", "");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("auth-token", token);
        return headers;
    }

    private Map<String, String> createRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        return params;
    }


    private void showListDialog() {

        ListDialog.showListDialog(this, "اللوحات", Plate);
    }


//    private void openInputActivity() {
//        // Create an AlertDialog.Builder instance
//        AlertDialog.Builder builder = new AlertDialog.Builder(AddedShopDetails.this);
//        View dialogView = getLayoutInflater().inflate(R.layout.inpot_billboard, null);
//        builder.setView(dialogView);
//
//
//// Create the AlertDialog
//        AlertDialog alertDialog2 = builder.create();
//
//// Show the AlertDialog
//        spinner = findViewById(R.id.board_type);
//        length = dialogView.findViewById(R.id.board_length);
//        width = dialogView.findViewById(R.id.board_length);
//        quan = dialogView.findViewById(R.id.board_quan);
//        upload_billboard_bt = dialogView.findViewById(R.id.upload_billboard_bt);
//
//
//        // Create a list of items for the Spinner
////        List<String> spinnerItems = new ArrayList<>();
////        spinnerItems.add("Item 1");
////        spinnerItems.add("Item 2");
////        spinnerItems.add("Item 3");
////
////        // Create an ArrayAdapter using the string array and a default Spinner layout
////        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
////
////        // Specify the layout to use when the list of choices appears
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        // Apply the adapter to the Spinner
////        spinner.setAdapter(adapter);
////
////        // Set a listener to handle Spinner item selection
////        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
////                // Get the selected item text
////                selectedItem = spinnerItems.get(position);
////
////                // Do something with the selected item text (e.g., display in a Toast)
////                Toast.makeText(AddedShopDetails.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onNothingSelected(AdapterView<?> parentView) {
////                // Do nothing here
////            }
////        });
//        upload_billboard_bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                type.getText().toString();
//                length.getText().toString();
//                width.getText().toString();
//                quan.getText().toString();
//                alertDialog2.dismiss();
//            }
//        });
//
//        alertDialog2.show();
//        }
}