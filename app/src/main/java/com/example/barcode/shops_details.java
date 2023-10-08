package com.example.barcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.barcode.pdf_report.BarCodeEncoder;
import com.example.barcode.pdf_report.TemplatePDF;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.ArrayList;

public class shops_details extends AppCompatActivity {
    private ImageView bt_exit;
     TextInputEditText id_no,last_licens,owner_name,shop_name,phone_no,activity_type,neighbor_unit,address_unit;
     TextView front_signboard,side_signboard,elec_signboard,supetficail_signboard,stuck_signboard,mural_signborard,totl_price;
     Button btnPdfReceipt, btnThermalPrinter;
     ContentLoadingProgressBar progressBar;
    private TemplatePDF templatePDF;
     String currency, shopname="احمد", shop_contact, shop_email, shop_address=String.valueOf("الرويشان") , longText, shortText;

     String insitiution_number="08967674490",order_time,order_date="احمد محمد";
    //how many headers or column you need, add here by using ,
    //headers and get clients para meter must be equal
    private String[] header = {"Description", "Price"};
    Bitmap bm = null;
    private static final int REQUEST_CONNECT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_details);
        progressBar=findViewById(R.id.all_details_progressBar);
        bt_exit=findViewById(R.id.exit);
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


        shortText = "Customer Name: Mr/Mrs. احمد" + "customer_name";
        longText = "Thanks for purchase. Visit again";


        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("دائرة الخدمات الالكترونية", "تراخيص المهن", "YQR");
        templatePDF.addTitle(shopname, shop_address + "\n ايميل" + shop_email + "\nContact: " + shop_contact + "\nInvoice ID:" + insitiution_number, order_time + " " + order_date);
        templatePDF.addParagraph(shortText);


        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
        try {
            bm = qrCodeEncoder.encodeAsBitmap(insitiution_number, BarcodeFormat.CODE_128, 600, 300);
        } catch (WriterException e) {
            Log.d("Data", e.toString());
        }


        btnPdfReceipt.setOnClickListener(v -> {

            templatePDF.createTable(header, getOrdersData());
            templatePDF.addRightParagraph(longText);
            templatePDF.addImage(bm);
            templatePDF.closeDocument();
            templatePDF.viewPDF();

        });

//        btnThermalPrinter.setOnClickListener(v -> {
//
//            //Check if the Bluetooth is available and on.
//            if (!Tools.isBlueToothOn(shops_details.this)) return;
//            PrefMng.saveActivePrinter(shops_details.this, PrefMng.PRN_WOOSIM_SELECTED);
//            //Pick a Bluetooth device
//            Intent i = new Intent(shops_details.this, DeviceListActivity.class);
//            startActivityForResult(i, REQUEST_CONNECT);
//        });

        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }

    private ArrayList<String[]> getOrdersData() {
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"الجوبي" + "\n" + "مفروشات" + "\n" + "(" + "qty" + "x" + currency + "price" + ")", currency + "cost_total"});
        rows.add(new String[]{"..........................................", ".................................."});
        rows.add(new String[]{"Sub Total: ", currency + "40000"});
        rows.add(new String[]{"Total Tax: ", currency + "اربعين الف ريال"});
        rows.add(new String[]{"Discount: ", currency + "getDiscount"});
        rows.add(new String[]{"..........................................", ".................................."});
        rows.add(new String[]{"Total Price: ", currency + "price"});
        return rows;
    }

}