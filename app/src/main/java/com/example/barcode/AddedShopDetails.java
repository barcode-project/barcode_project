package com.example.barcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barcode.Server.URLs;
import com.example.barcode.pdf_report.TemplatePDF;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddedShopDetails extends AppCompatActivity {
    private ImageView bt_exit;
    TextInputEditText id_no, last_licens, owner_name, shop_name, phone_no, activity_type, neighbor_unit, address_unit,type,length,width,quan;
    AppCompatButton show_billboard,add_btn;
    ContentLoadingProgressBar progressBar;
    Button upload_billboard_bt;
    private SharedPreferences sharedPreferences;
    private int id;
    ArrayAdapter<String> shopstypeAdapter;
    String selectedshopstypID;
    List<HashMap<String, String>> shopsCategory;
    List<String> name_shops_type;
    Spinner spinner;
    String selectedItem;
    private List<PlateData> Plate ;


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
        neighbor_unit=findViewById(R.id.neighbor_unit);
        address_unit=findViewById(R.id.address_unit);
        show_billboard = findViewById(R.id.show_billboard);
        add_btn = findViewById(R.id.add_btn);
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        id = getIntent().getIntExtra("id",0);
        Log.d("ALL_ID", String.valueOf(id));

        test();

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
    }


    private List<shops> test() {
        List<shops> shops = new ArrayList<>();
        List<PlateData> list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, URLs.Get_Vir_Org, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //                Log.d("ALL_SHOPS_RESPONSE", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")) {
                        JSONObject citizen = new JSONObject(object.getString("data"));
                        owner_name.setText(citizen.getString("owner_name"));
                        id_no.setText(String.valueOf(citizen.getInt("id")));
                        shop_name.setText(citizen.getString("org_name"));
                        phone_no.setText(citizen.getString("owner_phone"));
                        address_unit.setText(citizen.getString("street_name"));
                        activity_type.setText(citizen.getString("org_type_name"));
                        JSONArray array = new JSONArray(citizen.getString("billboard"));
                        for (int i = 0; i < array.length(); i++) {
                        JSONObject billboard = array.getJSONObject(i);
                        JSONObject type_billboard = billboard.getJSONObject("billboard");

                            PlateData board = new PlateData();
//                            user.setNo(i+1);
                            board.setPlateType(type_billboard.getString("name"));
                            board.setLength(String.valueOf(billboard.getDouble("height")));
                            board.setWidth(String.valueOf(billboard.getDouble("width")));
                            board.setQuantity(String.valueOf(billboard.getInt("count")));

                            list.add(board);

                        }
                        Plate = list;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddedShopDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(AddedShopDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                //                progressBar.setVisibility(View.GONE);
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
                HashMap<String,String> map = new HashMap<>();
                map.put("id",String.valueOf(id));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        return shops;
    }

    private void showListDialog() {
//        List<PlateData> items = new ArrayList<>();
//        items.add(new PlateData("جانبية", "1", "2","4"));
//        items.add(new PlateData("امامية", "1", "1","3"));

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