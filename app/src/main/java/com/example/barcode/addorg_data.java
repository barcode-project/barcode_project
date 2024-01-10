package com.example.barcode;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.example.barcode.Items.shops;
import com.example.barcode.Server.URLs;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class addorg_data extends AppCompatActivity implements OnMapReadyCallback {
    ProgressDialog loading;
    private static final int PICK_IMAGE_REQUEST = 123;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int THUMBNAIL_SIZE = 500;
    TextInputEditText address_unit, doors_numbers, owner_name, shop_name, phone_no, shop_type, note;
    Button uploadButton;
    ContentLoadingProgressBar progressBar;
    double latitude, longitude;
    RelativeLayout pickimagebtn,pickimagebtn2,pickimagebtn3;
    ViewPager viewPager;
    ImageView imageView,imageView2,imageView3;
    Uri ImageUri;
    ArrayAdapter<String> streetAdapter, shopstypeAdapter;
    ArrayList<Uri> chooseImageList;
//    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    List<String> name_street, name_shops_type;
    String selectedstreetID, selectedshopstypID, DoorsNumbers, OwnerName, ShopName, PhoneNo, ShopType, Note, NameStreet;
    List<HashMap<String, String>> productCategory, shopsCategory;
    private SharedPreferences sharedPreferences;
    private Bitmap bitmap,bitmap2,bitmap3 = null;
    private LatLng currentLatLng;
    private GoogleMap googleMap;
    private static final int PICK_IMAGE_REQUEST_1 = 1;
    private static final int PICK_IMAGE_REQUEST_2 = 2;
    private static final int PICK_IMAGE_REQUEST_3 = 3;
    private DataRepository dataRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorg_data);
        progressBar = findViewById(R.id.add_org_progressBar);
        pickimagebtn = findViewById(R.id.chooseImage);
        pickimagebtn2 = findViewById(R.id.chooseImage2);
        pickimagebtn3 = findViewById(R.id.chooseImage3);
        viewPager = findViewById(R.id.viewPager);
        ImageView add_shops_exit = findViewById(R.id.add_shops_exit);
        uploadButton = findViewById(R.id.upload_bt);
        address_unit = findViewById(R.id.address_unit);
        doors_numbers = findViewById(R.id.doors_numbers);
        owner_name = findViewById(R.id.owner_name);
        shop_name = findViewById(R.id.shop_name);
        shop_type = findViewById(R.id.shop_type);
        phone_no = findViewById(R.id.phone_no);
        note = findViewById(R.id.note);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        GPSUtils gpsUtils = new GPSUtils(this);
        gpsUtils.statusCheck(findViewById(android.R.id.content));

        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        chooseImageList = new ArrayList<>();

        name_street = new ArrayList<>();
        name_shops_type = new ArrayList<>();

        test();


        address_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streetAdapter = new ArrayAdapter<String>(addorg_data.this, android.R.layout.simple_list_item_1);
                streetAdapter.addAll(name_street);

                AlertDialog.Builder dialog = new AlertDialog.Builder(addorg_data.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialog_button = dialogView.findViewById(R.id.dialog_button);
                EditText dialog_input = dialogView.findViewById(R.id.dialog_input);
                TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);

                dialog_title.setText("الشارع");
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(streetAdapter);

                dialog_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        streetAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                final AlertDialog alertDialog = dialog.create();

                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialog.dismiss();
                        final String selectedItem = streetAdapter.getItem(position);

                        String street_id = "1";
                        address_unit.setText(selectedItem);

                        for (int i = 0; i < name_street.size(); i++) {
                            if (name_street.get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                street_id = productCategory.get(i).get("street_id");
                            }
                        }

                        selectedstreetID = street_id;
//
//                        // حفظ المتغير في SharedPreferences باستخدام DataRepository
//                        DataRepository dataRepository = new DataRepository(addorg_data.this);
//                        List<String> savedStreetIDs = dataRepository.loadStreetIDs();
//
//                        if (savedStreetIDs == null) {
//                            savedStreetIDs = new ArrayList<>();
//                        }
//
//                        savedStreetIDs.add(selectedstreetID);
//                        dataRepository.saveStreetIDs(savedStreetIDs);

                        Log.d("street_id", street_id);
                    }
                });
            }
        });

        shop_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopstypeAdapter = new ArrayAdapter<String>(addorg_data.this, android.R.layout.simple_list_item_1);
                shopstypeAdapter.addAll(name_shops_type);

                AlertDialog.Builder dialog = new AlertDialog.Builder(addorg_data.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialog_button = dialogView.findViewById(R.id.dialog_button);
                EditText dialog_input = dialogView.findViewById(R.id.dialog_input);
                TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                SwipeRefreshLayout swipeRefreshLayout = dialogView.findViewById(R.id.swipe);

                dialog_title.setText("نوع النشاط");
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(shopstypeAdapter);

                swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    test();
                }, 1000));

                dialog_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        shopstypeAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                final AlertDialog alertDialog = dialog.create();

                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialog.dismiss();
                        final String selectedItem = shopstypeAdapter.getItem(position);

                        String shopstype_id = "1";
                        shop_type.setText(selectedItem);

                        for (int i = 0; i < name_shops_type.size(); i++) {
                            if (name_shops_type.get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                shopstype_id = shopsCategory.get(i).get("shopstype_id");
                            }
                        }
                        Toast.makeText(addorg_data.this, selectedshopstypID, Toast.LENGTH_SHORT).show();
                        Log.d("shopstype_id", shopstype_id);
                    }
                });
            }
        });

        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkpermission(PICK_IMAGE_REQUEST_1);

            }
        });
        pickimagebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checkpermission(PICK_IMAGE_REQUEST_2);

            }
        });
        pickimagebtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checkpermission(PICK_IMAGE_REQUEST_3);

            }
        });
        add_shops_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    saveData();
                }
            }
        });

    }



    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            requestLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void Checkpermission(int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(addorg_data.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(addorg_data.this, new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA}, PICK_IMAGE_REQUEST);
            } else {

                pickimageftomgallry(requestCode);

            }
        } else {

            pickimageftomgallry(requestCode);

        }
    }

    private void pickimageftomgallry(int requestCode) {
        Intent intent = new Intent(addorg_data.this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
        String imagePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
        ImageUri = Uri.parse(imagePath);
            switch (requestCode) {
                case PICK_IMAGE_REQUEST_1:
                    imageView.setImageURI(ImageUri);
                    try {
                        // Use the imageUri to open an InputStream and decode it into a Bitmap
//                        File f = new File(ImageUri.getPath());
//                        Uri test = Uri.fromFile(f);
//                        InputStream inputStream = getContentResolver().openInputStream(ImageUri);
                        bitmap = BitmapFactory.decodeFile(imagePath);
//                        bitmap = BitmapFactory.decodeStream(inputStream);
//                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("bitmap_IMG_ORG", e.toString());
                    }
                    break;
                case PICK_IMAGE_REQUEST_2:
                    imageView2.setImageURI(ImageUri);
                    try {
                        // Use the imageUri to open an InputStream and decode it into a Bitmap
//                        File f = new File(ImageUri.getPath());
//                        Uri test = Uri.fromFile(f);
//                        InputStream inputStream = getContentResolver().openInputStream(ImageUri);
//                        bitmap2 = BitmapFactory.decodeStream(inputStream);
                        bitmap2 = BitmapFactory.decodeFile(imagePath);
//                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("bitmap_IMG_ORG", e.toString());
                    }
                    break;
                case PICK_IMAGE_REQUEST_3:
                    imageView3.setImageURI(ImageUri);
                    try {
                        // Use the imageUri to open an InputStream and decode it into a Bitmap
//                        File f = new File(ImageUri.getPath());
//                        Uri test = Uri.fromFile(f);
//                        InputStream inputStream = getContentResolver().openInputStream(ImageUri);
//                        bitmap3 = BitmapFactory.decodeStream(inputStream);
                        bitmap3 = BitmapFactory.decodeFile(imagePath);
//                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("bitmap_IMG_ORG", e.toString());
                    }
                    break;
            }


////            chooseImageList.add(ImageUri);
////            SetAdapter();
////            ImageUri = Uri.parse(data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH));
////            imageView.setImageURI(ImageUri);
//
//            // تحقق من أن النص (Path) الذي تم استخدامه لإنشاء ملف (File) صحيح
//            File f = new File(ImageUri.getPath());
//            Uri test = Uri.fromFile(f);
//            try {
//                InputStream inputStream = getContentResolver().openInputStream(test);
//
//                // تحسين طريقة تحويل Uri إلى Bitmap باستخدام BitmapFactory.Options
////                BitmapFactory.Options options = new BitmapFactory.Options();
////                options.inSampleSize = 4; // قلل حجم الصورة إلى الربع
////                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
//                bitmap = BitmapFactory.decodeStream(inputStream);
////                bitmapList.add(bitmap);
////                String s = bitmapToString(bitmap);
////                Log.d("BITMAP",s);
//                inputStream.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d("bitmap_IMG_ORG", e.toString());
//            }
        }
    }




//    private void SetAdapter() {
//
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this, chooseImageList);
//        viewPager.setAdapter(adapter);
//
//    }

    private void requestLocationUpdates() {
        if (googleMap != null) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        setCurrentLocation();

                        return true;
                    }

                });
                // حصول على الموقع الحالي
                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                        setCurrentLocation();

                        // التحرك إلى الموقع الحالي
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18));
                    }

                });
            }
        }
    }

    private void setCurrentLocation() {
        if (currentLatLng != null) {
            latitude = currentLatLng.latitude;
            longitude = currentLatLng.longitude;
            String locationText = "Latitude: " + latitude + "\nLongitude: " + longitude;

            // عرض معلومات الموقع في واجهة المستخدم، أو استخدمها في عمليات أخرى
            Toast.makeText(this, "الموقع المحدد: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                if (googleMap != null) {
                    requestLocationUpdates();
                }else {
                    Toast.makeText(this, "لايمكن الوصول الى احداثيات الموقع", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickimageftomgallry(requestCode);
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void test() {
        ArrayList<HashMap<String, String>> product_category = new ArrayList<>();
        ArrayList<HashMap<String, String>> shops_category = new ArrayList<>();
        loading = new ProgressDialog(addorg_data.this);
        loading.setMessage("انتظر من فضلك. . .");
        loading.setCancelable(false);
        loading.show();
        StringRequest request = new StringRequest(Request.Method.GET, URLs.Get_Streets, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")) {
                        JSONArray array = new JSONArray(object.getString("data"));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject citizen = array.getJSONObject(i);
//                            user.setNo(i+1);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("street_id", String.valueOf(citizen.getInt("id")));
                            map.put("name_street", citizen.getString("name"));
                            product_category.add(map);
                            Log.d("ALL_SHOPS_RESPONSE", String.valueOf(citizen));
                        }

                        JSONArray array2 = new JSONArray(object.getString("org_type"));
                        for (int i = 0; i < array2.length(); i++) {
                            JSONObject citizen = array2.getJSONObject(i);
//                            user.setNo(i+1);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("shopstype_id", String.valueOf(citizen.getInt("id")));
                            map.put("name_shops_type", citizen.getString("name"));
                            Log.d("ALL_SHOPS_RESPONSE", response);
                            shops_category.add(map);
                            Log.d("ALL_SHOPS_RESPONSE", String.valueOf(citizen));
                        }

                    }
                    for (int i = 0; i < product_category.size(); i++) {
                        // Get the ID of selected Country
                        name_street.add(product_category.get(i).get("name_street"));
                    }
                    productCategory = product_category;


                    for (int i = 0; i < shops_category.size(); i++) {
                        // Get the ID of selected Country
                        name_shops_type.add(shops_category.get(i).get("name_shops_type"));
                    }
                    shopsCategory = shops_category;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(addorg_data.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "خطأ غير معروف";
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
                    }else if (statusCode == 443) {
                        errorMessage = "خطاء في الشهادة الامان.";
                    }
                }
                Toast.makeText(addorg_data.this, errorMessage, Toast.LENGTH_SHORT).show();
                loading.dismiss();
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
    }


    private void saveData() {
        loading = new ProgressDialog(addorg_data.this);
        loading.setMessage("انتظر من فضلك. . .");
        loading.setCancelable(false);
        loading.show();
        List<shops> shops = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, URLs.Insert_Data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_jk", response);
                try {
                    JSONObject object = new JSONObject(response);


                    if (object.getBoolean("success")) {
                        Toast.makeText(addorg_data.this, "تمت الاضافة", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(addorg_data.this, AddedOrgsList.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(addorg_data.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(addorg_data.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    // يمكنك إضافة المزيد من الحالات حسب احتياجاتك
                }
                Toast.makeText(addorg_data.this, errorMessage, Toast.LENGTH_SHORT).show();
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
                map.put("org_name", shop_name.getText().toString().trim());
                map.put("owner_name", owner_name.getText().toString().trim());
                map.put("owner_phone", phone_no.getText().toString().trim());
                map.put("building_type_id", "1");
                map.put("org_type_id", selectedshopstypID);
                map.put("street_id", selectedstreetID);
                map.put("hood_unit_id", selectedstreetID);
                map.put("note", note.getText().toString().trim());
                map.put("log_x", String.valueOf(latitude));
                map.put("log_y", String.valueOf(longitude));
                map.put("org_image",bitmapToString(bitmap));
//                map.put("org_image2",bitmapToString(bitmap2));
//                map.put("org_image3",bitmapToString(bitmap3));

                return map;
            }



        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
//        MySingleton.getInstance(this).getRequestQueue().add(request);

    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.NO_WRAP);

        }

        return "";
    }

    private boolean validate() {
         if (shop_name.getText().toString().isEmpty()) {
            shop_name.setError(getString(R.string.this_cannot_be_empty));
            shop_name.requestFocus();
            return false;
        } else if (doors_numbers.getText().toString().isEmpty()) {
            doors_numbers.setError(getString(R.string.this_cannot_be_empty));
            doors_numbers.requestFocus();
            return false;
        } else if (shop_type.getText().toString().isEmpty() || selectedshopstypID.isEmpty()) {
            shop_type.setError(getString(R.string.this_cannot_be_empty));
            shop_type.requestFocus();
            return false;
        } else if (address_unit.getText().toString().isEmpty() || selectedstreetID.isEmpty()) {
            address_unit.setError(getString(R.string.this_cannot_be_empty));
            address_unit.requestFocus();
            return false;
        }
        return true;
    }

}
