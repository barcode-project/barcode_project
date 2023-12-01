package com.example.barcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.barcode.pdf_report.BarCodeEncoder;
import com.example.barcode.pdf_report.TemplatePDF;
import com.example.barcode.utils.PrefMng;
import com.example.barcode.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class shops_details extends AppCompatActivity {

    private TemplatePDF templatePDF;
    private ImageView bt_exit;
    TextInputEditText id_no,last_licens,owner_name,shop_name,phone_no,activity_type,neighbor_unit,address_unit;
    TextView front_signboard,side_signboard,elec_signboard,supetficail_signboard,stuck_signboard,mural_signborard,totl_price;
    Button btnPdfReceipt, btnThermalPrinter;
    ContentLoadingProgressBar progressBar;

     String currency="", shopname="احمد", shop_contact, shop_email, shop_address=String.valueOf("الرويشان") , shortText;

     String insitiution_number="08967674490",order_time,order_date="12/12/2000";
    //how many headers or column you need, add here by using ,
    //headers and get clients para meter must be equal
    private String[] header = {"المبلغ", "الرسوم"};
    private  String[] shortTex = {null},longText;
    Bitmap bm = null;
    private static final int REQUEST_CONNECT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_details);
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
        front_signboard=findViewById(R.id.front_signboard);
        side_signboard=findViewById(R.id.side_signboard);
        elec_signboard=findViewById(R.id.elec_signboard);
        supetficail_signboard=findViewById(R.id.supetficail_signboard);
        stuck_signboard=findViewById(R.id.stuck_signboard);
        mural_signborard=findViewById(R.id.mural_signborard);
        totl_price=findViewById(R.id.totl_price);
        btnThermalPrinter=findViewById(R.id.printBtn);
        btnPdfReceipt=findViewById(R.id.save_pdf_Btn);
        int id = getIntent().getIntExtra("id",0);

        shortText = "Customer Name: Mr/Mrs. " + "customer_name";

        longText = new String[]{"نشكركم"};

        ArrayList<PlateData> plateList = new ArrayList<>();
//        plateList.add(new PlateData("نوع اللوحة 1", 5, 100.0, 50.0));
        plateList.add(new PlateData("نوع اللوحة 1", 5, 100.0, 50.0));
//        plateList.add(new PlateData("نوع اللوحة 2", 5, 100.0, 50.0));
        plateList.add(new PlateData("نوع اللوحة 2", 3, 80.0, 40.0));
        // ... أضف المزيد من البيانات إذا لزم الأمر
//        String[] items={"5, 100.0, 50.0","411","412","413","415"};
        String[] items={"5, 100.0, 50.0","411","412","413","415"};
        // قم بربط بيانات اللوحة بـ ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

//     ArrayAdapter<PlateData> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, items);
//
        // ربط القائمة بـ ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);


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
            templatePDF = new TemplatePDF(getApplicationContext());
            templatePDF.openDocument();
            //templatePDF.addMetaData("دائرة الخدمات الالكترونية", "تراخيص المهن", "YQR");
            //templatePDF.addTitle(shopname, shop_address + "\n ايميل" + shop_email + "\nContact: " + shop_contact + "\nInvoice ID:" + insitiution_number, order_time + " " + order_date);
            // templatePDF.addParagraph(shortText);
            templatePDF.createHeader(shortTex,gethaderData());
            templatePDF.createTable(header, getOrdersData());
            templatePDF.addRightParagraph(longText);
            templatePDF.addImage(bm);
            templatePDF.closeDocument();
            templatePDF.viewPDF();

        });

        btnThermalPrinter.setOnClickListener(v -> {

            //Check if the Bluetooth is available and on.
            if (!Tools.isBlueToothOn(shops_details.this)) return;
            PrefMng.saveActivePrinter(shops_details.this, PrefMng.PRN_WOOSIM_SELECTED);
            //Pick a Bluetooth device
            Intent i = new Intent(shops_details.this, DeviceListActivity.class);
            startActivityForResult(i, REQUEST_CONNECT);
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
                double latitude = 37.7749; // خط العرض لموقع العميل
                double longitude = -122.4194; // خط الطول لموقع العميل
                String label = "موقع العميل"; // اسم الموقع الذي سيظهر في خرائط جوجل

                String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + label + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // تحديد تطبيق خرائط جوجل

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });



    }

    private ArrayList<String[]> getOrdersData() {
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{currency + shopname,"رسوم محل"});
        rows.add(new String[]{currency + "40000","دعاية وإعلان"});
        rows.add(new String[]{currency + "2000","نظافة"});
        rows.add(new String[]{currency + "1200","نظافة مهن"});
        rows.add(new String[]{currency + "2500","رسوم الخدمة"});
        rows.add(new String[]{"..................................", "......................................."});
        rows.add(new String[]{currency + "price","الإجمالي"});

        return rows;
    }
    private ArrayList<String[]> gethaderData() {
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{ shopname});
        rows.add(new String[]{"دائرة الخدمات الالكترونية"});
        rows.add(new String[]{"مديرية الوحدة"});
        rows.add(new String[]{"نظافة مهن"});
        rows.add(new String[]{"نوع النشاط"});
        rows.add(new String[]{"اسم المالك"});

        return rows;
    }

}