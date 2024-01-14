package com.example.barcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;

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
import com.example.barcode.pdf_report.BarCodeEncoder;
import com.example.barcode.pdf_report.TemplatePDF;
import com.example.barcode.utils.PrefMng;
import com.example.barcode.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class shops_details extends AppCompatActivity {
    ProgressDialog loading;

    private TemplatePDF templatePDF;
    private ImageView bt_exit;
    TextInputEditText id_no, last_licens, owner_name, shop_name, phone_no, activity_type, neighbor_unit, address_unit, last_license;
    Button btnPdfReceipt, btnThermalPrinter;
    AppCompatButton show_billboard;
    ContentLoadingProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    DecimalFormat f;
    String currency = "R ", activitytype, shopname, shop_contact, ownername, longitudelength, latitudewidth, shortText, local_fee, clean_pay,clean, total_ad, el_gate, office_txt, direct_txt, s4;
    double total_price, getlocal_fee, getclean_pay, gettotal_ad, getel_gate,clean_price;

    //how many headers or column you need, add here by using ,
    //headers and get clients para meter must be equal
    private String[] header = {"المبلغ", "الرسوم"};
    private String[] shortTex = {null}, longText;
    Bitmap bm = null;
    private static final int REQUEST_CONNECT = 100;
    private int id;
    private List<PlateData> Plate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_details);
        progressBar = findViewById(R.id.all_details_progressBar);
        bt_exit = findViewById(R.id.shops_exit1);
        id_no = findViewById(R.id.insitiution_no);
        last_licens = findViewById(R.id.last_license);
        owner_name = findViewById(R.id.owner_name);
        shop_name = findViewById(R.id.shop_name);
        phone_no = findViewById(R.id.phone_no);
        activity_type = findViewById(R.id.activity_type);
        neighbor_unit = findViewById(R.id.neighbor_unit);
        address_unit = findViewById(R.id.address_unit);
        show_billboard = findViewById(R.id.show_billboard);
        btnThermalPrinter = findViewById(R.id.printBtn);
        btnPdfReceipt = findViewById(R.id.save_pdf_Btn);
        last_license=findViewById(R.id.last_license);
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        id = getIntent().getIntExtra("id", 0);
        direct_txt = String.valueOf("مديرية " + sharedPreferences.getString("direct_name", ""));
        office_txt = String.valueOf("مكتب " + sharedPreferences.getString("office_name", ""));
        Log.d("id", String.valueOf(id));
        f = new DecimalFormat("#0");

        test();

//        longText = new String[]{s4};


//            try {
//                bm= createQRCode(insitiution_number,600,300);
//            } catch (WriterException e) {
//                throw new RuntimeException(e);
//            }




        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
//        try {
//            bm = qrCodeEncoder.encodeAsBitmap(insitiution_number, BarcodeFormat.CODE_128, 600, 300);
//        } catch (WriterException e) {
//            Log.d("Data", e.toString());
//        }


        btnPdfReceipt.setOnClickListener(v -> {
            try {
                templatePDF = new TemplatePDF(getApplicationContext());
                templatePDF.openDocument();
                //templatePDF.addMetaData("دائرة الخدمات الالكترونية", "تراخيص المهن", "YQR");
                //templatePDF.addTitle(shopname, shop_address + "\n ايميل" + shop_email + "\nContact: " + shop_contact + "\nInvoice ID:" + insitiution_number, order_time + " " + order_date);
                // templatePDF.addParagraph(shortText);
                templatePDF.createHeader(shortTex, gethaderData());
                templatePDF.createTable(header, getOrdersData());
                templatePDF.addRightParagraph(longText);
                templatePDF.addImage(bm);
                templatePDF.closeDocument();
                templatePDF.viewPDF();
            }catch (Exception e)  {
                e.printStackTrace();
                Toast.makeText(shops_details.this,  "لاتوجد حافظة\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        btnThermalPrinter.setOnClickListener(v -> {

            //Check if the Bluetooth is available and on.
            if (!Tools.isBlueToothOn(shops_details.this)) return;
            PrefMng.saveActivePrinter(shops_details.this, PrefMng.PRN_WOOSIM_SELECTED);
            //Pick a Bluetooth device
            Intent i = new Intent(shops_details.this, DeviceListActivity.class);
            startActivityForResult(i, REQUEST_CONNECT);
        });

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
        FloatingActionButton button=findViewById(R.id.fab);
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
                    Toast.makeText(shops_details.this,  "لاتوجد احداثيات\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private ArrayList<String[]> getOrdersData() {
        gettotal_ad = Double.valueOf(total_ad);
        getclean_pay = Double.valueOf(clean_pay);
        getlocal_fee = Double.valueOf(local_fee);
        getel_gate = Double.valueOf(el_gate);
        clean_price = Double.valueOf(clean);
        total_price = gettotal_ad + getclean_pay + getlocal_fee + getel_gate+clean_price;
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{currency + f.format(getel_gate), "رسوم البوابة الالكترونية"});
        rows.add(new String[]{currency + f.format(getlocal_fee), "الرسوم المحلية"});
        rows.add(new String[]{currency + f.format(gettotal_ad), "الدعاية والاعلان"});
        rows.add(new String[]{currency + f.format(clean_price), "نظافة"});
        rows.add(new String[]{currency + f.format(getclean_pay), "نظافة المهن"});
        rows.add(new String[]{"..................................", "......................................."});
        rows.add(new String[]{currency + f.format(total_price), "الإجمالي"});
        return rows;
    }
    private ArrayList<String[]> gethaderData() {
        ArrayList<String[]> rows = new ArrayList<>();
//        rows.add(new String[]{ shopname});
        rows.add(new String[]{"دائرة الخدمات الالكترونية"});
        rows.add(new String[]{direct_txt});
        rows.add(new String[]{office_txt});
        rows.add(new String[]{"الوقت والتاريخ"});
        rows.add(new String[]{" نوع النشاط: " + activitytype});
        rows.add(new String[]{"اسم المنشأه: " + shopname});
        rows.add(new String[]{"اسم المالك: " + ownername});
        rows.add(new String[]{"رقم التواصل"});

        return rows;
    }

        private void test() {
            loading = new ProgressDialog(shops_details.this);
            loading.setMessage("انتظر من فضلك. . .");
            loading.setCancelable(false);
            loading.show();
            List<shops> shops = new ArrayList<>();
            List<PlateData> list = new ArrayList<>();
            StringRequest request = new StringRequest(Request.Method.POST, URLs.Get_Org, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("ALL_SHOPS_RESPONSE", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("success")) {
                            JSONObject citizen = object.getJSONObject("data");
                            owner_name.setText(citizen.getString("owner_name"));
                            id_no.setText(String.valueOf(citizen.getInt("id")));
                            shop_name.setText(citizen.getString("org_name"));
                            phone_no.setText(citizen.getString("owner_phone"));
                            address_unit.setText(citizen.getString("street_name"));
                            activity_type.setText(citizen.getString("org_type_name"));
                            last_license.setText(citizen.getString("license_status"));
//                            shownote.setText(citizen.getString("note"));
                            longitudelength = citizen.getString("log_y");
                            latitudewidth = citizen.getString("log_x");
                            JSONArray array = new JSONArray(citizen.getString("billboard"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject billboard = array.getJSONObject(i);
                                JSONObject type_billboard = billboard.getJSONObject("billboard");

                                PlateData board = new PlateData();
                                board.setPlateType(type_billboard.getString("name"));
                                board.setLength(String.valueOf(billboard.getDouble("height")));
                                board.setWidth(String.valueOf(billboard.getDouble("width")));
                                board.setQuantity(String.valueOf(billboard.getInt("count")));

                                list.add(board);
                            }
                            Plate = list;
                            JSONObject array2 = citizen.getJSONObject("clip_board");
                            el_gate = String.valueOf(array2.getInt("el_gate"));//'رسوم البوابة الالكترونية'
                            local_fee = String.valueOf(array2.getInt("local_fee"));//('اجمالي الرسوم المحلية
                            clean_pay = String.valueOf(array2.getInt("clean_pay"));//اجمالي ؤسوم نظافة المهن
                            clean = String.valueOf(array2.getInt("clean"));//اجمالي ؤسوم نظافة
                            total_ad = String.valueOf(array2.getInt("total_ad"));//اجمالي رسوم الدعاية والاعلان
                            s4 = array2.getString("clip_status");


                            Log.d("ALL_SHOPS_RESP", String.valueOf(citizen));
                        }

                        shopname = String.valueOf(shop_name.getText());
                        ownername = String.valueOf(owner_name.getText());
                        activitytype = String.valueOf(activity_type.getText());


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(shops_details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
//                    progressDialog.dismiss();
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
                    }
                    Toast.makeText(shops_details.this, errorMessage, Toast.LENGTH_SHORT).show();
    //                progressBar.setVisibility(View.GONE);
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
        }

    private void showListDialog() {

        ListDialog.showListDialog(this, "اللوحات", Plate);

    }
}