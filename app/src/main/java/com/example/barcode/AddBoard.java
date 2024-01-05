package com.example.barcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barcode.Items.shops;
import com.example.barcode.Server.URLs;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBoard extends AppCompatActivity {
    TextInputEditText length,width,quan;
    Spinner type;
    Button upload_billboard_bt;
    ContentLoadingProgressBar progressBar;
    SharedPreferences sharedPreferences;
    private int id;
    private int index;
    private String selectedValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);
        type = findViewById(R.id.board_type);
        length = findViewById(R.id.board_length);
        width = findViewById(R.id.board_width);
        quan = findViewById(R.id.board_quan);
        upload_billboard_bt = findViewById(R.id.upload_billboard_bt);
        progressBar = findViewById(R.id.add_org_progressBar);
        id = getIntent().getIntExtra("id",0);


        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        upload_billboard_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                }
                    saveData();
            }
        });

        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("جانبية");
        spinnerItems.add("امامي");
        spinnerItems.add("جدارية");
        spinnerItems.add("لواصق");
        spinnerItems.add("سطحية");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the Spinner
        type.setAdapter(adapter);

        // Set a listener to handle Spinner item selection
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item text
                selectedValue = parentView.getItemAtPosition(position).toString();
                index = position+1;
                Log.d("ALL_ID", String.valueOf(index));

                // Do something with the selected item text (e.g., display in a Toast)
                Toast.makeText(AddBoard.this, "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Now you can use 'selectedValue' outside the listener
        // For example, you can access it in a button click listener or any other part of your code
        // ...
    }


    private boolean validate() {
        if (selectedValue.isEmpty()){
            Toast.makeText(this, "قم باختيار نوع اللوحة", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(length.getText().toString().isEmpty()){
            length.setError("ادخل قيمة");
            length.setFocusable(true);
            return false;
        } else if(width.getText().toString().isEmpty()){
            width.setError("ادخل قيمة");
            width.setFocusable(true);
            return false;
        } else if(quan.getText().toString().isEmpty()){
            quan.setError("ادخل قيمة");
            quan.setFocusable(true);
            return false;
        }
        return true;
    }

    private List<shops> saveData() {
        progressBar.setVisibility(View.VISIBLE);
        List<shops> shops = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, URLs.Insert_Board, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ALL_SHOPS_RESPONSE", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")) {
                        Toast.makeText(AddBoard.this, "تمت الاضافة", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddBoard.this,AddedShopDetails.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddBoard.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddBoard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(AddBoard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
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

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("height", length.getText().toString().trim());
                map.put("width", width.getText().toString().trim());
                map.put("count", quan.getText().toString().trim());
                map.put("billboard_id", String.valueOf(index));
                map.put("vir_org_id", String.valueOf(id));

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
//        MySingleton.getInstance(this).getRequestQueue().add(request);

        return shops;
    }
}