package com.example.barcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class resetpass extends AppCompatActivity {
    ProgressDialog loading;
    TextInputEditText old_pass,new_pass;
    AppCompatButton resetpassbtn;
    TextInputLayout layout_new_pass,layout_old_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);

        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);
        layout_new_pass = findViewById(R.id.layout_new_pass);
        layout_old_pass = findViewById(R.id.layout_old_pass);
        resetpassbtn = findViewById(R.id.resetpassbtn);

        resetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    reset();
                }
            }
        });

    }

    private void reset (){
        loading = new ProgressDialog(resetpass.this);
        loading.setMessage("انتظر من فضلك. . .");
        loading.setCancelable(false);
        loading.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLs.Reset_Password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")){
                        //make shared preference user
                        SharedPreferences userPref = getBaseContext().getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putBoolean("isReset",true);
                        startActivity(new Intent(resetpass.this, MainActivity.class));
                        finish();
                        editor.apply();
                        //if success
                        Toast.makeText(getBaseContext(), "تم تسجيل الدخول", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(resetpass.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(resetpass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
//                Toast.makeText(resetpass.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                String errorMessage = "حدث خطأ أثناء تسجيل الدخول. الرجاء معاودة المحاولة في وقت لاحق.";
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    errorMessage = "لا يمكن الاتصال بالخادم. الرجاء التحقق من اتصال الانترنت الخاص بك.";
                } else if (error instanceof AuthFailureError) {
                    errorMessage = "اسم المستخدم أو كلمة المرور غير صحيحة. حاول مرة اخرى.";
                } else if (error instanceof ServerError) {
                    errorMessage = "حدث خطأ في الخادم. الرجاء معاودة المحاولة في وقت لاحق.";
                } else if (error instanceof NetworkError) {
                    errorMessage = "لا يمكن الاتصال بالخادم. الرجاء التحقق من اتصال الانترنت الخاص بك.";
                } else if (error instanceof ParseError) {
                    errorMessage = "حدث خطأ أثناء تحليل الاستجابة. الرجاء معاودة المحاولة في وقت لاحق.";
                }
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }){

            // add parameters

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("old_pass",old_pass.getText().toString().trim());
                map.put("new_pass",new_pass.getText().toString().trim());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences userPref = getBaseContext().getSharedPreferences("user",MODE_PRIVATE);
                String token = userPref.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
//                map.put("Authorization","Bearer "+token);
                map.put("auth-token", token);
                return map;
            }
        };

        //add this request to requestqueue
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        queue.add(request);

    }

    private boolean validate() {
        //first getting the values
        final String mySSn = old_pass.getText().toString().trim();
        final String myPassword = new_pass.getText().toString().trim();

        //validating inputs
        if (TextUtils.isEmpty(mySSn)) {
            layout_old_pass.setError("ادخل كلمة المرور القديمة");
            layout_old_pass.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(myPassword)) {
            layout_new_pass.setError("ادخل كلمة المرور الجديدة");
            layout_new_pass.requestFocus();
            return false;
        }
        return true;
    }
}