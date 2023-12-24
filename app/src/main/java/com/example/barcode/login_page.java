package com.example.barcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barcode.Server.URLs;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login_page extends AppCompatActivity {
    Button signInBtn;
    private TextInputEditText usernamekey,Passwordtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        signInBtn=findViewById(R.id.signInBtn);
        usernamekey = findViewById(R.id.username);
        Passwordtext = findViewById(R.id.password);

        signInBtn.setOnClickListener(v -> {
            if (validate()){
                login();
//                startActivity(new Intent(login_page.this, MainActivity.class));
//                finish();

            }
        });

    }
    private void login (){
//        dialog.setMessage("Logging in");
//        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URLs.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("ALL_SHOPS_User", response);
                    if (object.getBoolean("success")){
                        JSONObject user = object.getJSONObject("data");
                        //make shared preference user
                        SharedPreferences userPref = getBaseContext().getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("token",user.getString("api_token"));
                        editor.putString("phone",user.getString("phone"));
                        editor.putString("fullname",user.getString("fullname"));
                        editor.putString("office_name",user.getString("office_name"));
                        editor.putString("direct_name",user.getString("direct_name  "));
//                        editor.putInt("id",user.getInt("id"));
                        editor.putBoolean("isLoggedIn",true);
                        editor.putString("type","user");
//                        Log.d("ALL_SHOPS_User", String.valueOf(user));
                        if(Passwordtext.getText().toString().trim().equals(usernamekey.getText().toString().trim()) ){
                            editor.apply();
                            startActivity(new Intent(login_page.this, resetpass.class));
                            finish();
                        }
                        else {
                            editor.putBoolean("isReset",true);
                            editor.apply();
                            startActivity(new Intent(login_page.this, MainActivity.class));
                            finish();
                        }
                        //if success
                        Toast.makeText(getBaseContext(), "تم تسجيل الدخول", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(login_page.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(login_page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                dialog.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
                error.printStackTrace();
                Toast.makeText(login_page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                String errorMessage = "حدث خطأ أثناء تسجيل الدخول. الرجاء معاودة المحاولة في وقت لاحق.";
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    errorMessage = "لا يمكن الاتصال بالخادم. الرجاء التحقق من اتصال الانترنت الخاص بك.";
//                } else if (error instanceof AuthFailureError) {
//                    errorMessage = "اسم المستخدم أو كلمة المرور غير صحيحة. حاول مرة اخرى.";
//                } else if (error instanceof ServerError) {
//                    errorMessage = "حدث خطأ في الخادم. الرجاء معاودة المحاولة في وقت لاحق.";
//                } else if (error instanceof NetworkError) {
//                    errorMessage = "لا يمكن الاتصال بالخادم. الرجاء التحقق من اتصال الانترنت الخاص بك.";
//                } else if (error instanceof ParseError) {
//                    errorMessage = "حدث خطأ أثناء تحليل الاستجابة. الرجاء معاودة المحاولة في وقت لاحق.";
//                }
//                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }){

            // add parameters

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("phone",usernamekey.getText().toString().trim());
                map.put("password",Passwordtext.getText().toString().trim());
                return map;
            }
        };

        //add this request to requestqueue
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        queue.add(request);

    }

    private boolean validate() {
        //first getting the values
        final String mySSn = usernamekey.getText().toString().trim();
        final String myPassword = Passwordtext.getText().toString().trim();

        //validating inputs
        if (TextUtils.isEmpty(mySSn)) {
            usernamekey.setError("Please enter your SSN");
            usernamekey.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(myPassword)) {
            Passwordtext.setError("Please enter your password");
            Passwordtext.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validate2() {
        //first getting the values
        final String mySSn = usernamekey.getText().toString().trim();
        final String myPassword = Passwordtext.getText().toString().trim();

        //validating inputs
        if (mySSn == "11" && myPassword == "11" ) {
            return true;
        }
        Toast.makeText(this, "خطأ في اسم المستخدم او كلمة المرور", Toast.LENGTH_SHORT).show();
            return false;

    }

}