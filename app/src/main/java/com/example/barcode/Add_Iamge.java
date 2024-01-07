package com.example.barcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class Add_Iamge extends AppCompatActivity {
    private int id;
    RelativeLayout pickimagebtn;
    ImageView imageView;
    TextInputEditText note;
    Uri ImageUri;
    Bitmap bitmap;
    ProgressDialog loading;
    Button upload_bt;
    private SharedPreferences sharedPreferences;
    private static final int PICK_IMAGE_REQUEST = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_iamge);
        pickimagebtn = findViewById(R.id.chooseImage);
        imageView = findViewById(R.id.imageView);
        note = findViewById(R.id.note);
        id = getIntent().getIntExtra("id",0);
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checkpermission();

            }
        });
    }
    private void Checkpermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Add_Iamge.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Add_Iamge.this, new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
            } else {

                pickimageftomgallry();

            }
        } else {

            pickimageftomgallry();

        }
    }
    private void pickimageftomgallry() {
        Intent intent = new Intent(Add_Iamge.this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            String imagePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            ImageUri = Uri.parse(imagePath);
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

            }
    }

    private void saveData() {
        loading = new ProgressDialog(Add_Iamge.this);
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
                        Toast.makeText(Add_Iamge.this, "تمت الاضافة", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Add_Iamge.this, AddedOrgsList.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Add_Iamge.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Add_Iamge.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Add_Iamge.this, errorMessage, Toast.LENGTH_SHORT).show();
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
                map.put("org_image",bitmapToString(bitmap));
                map.put("vir_org_id", String.valueOf(id));

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
}