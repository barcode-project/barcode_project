package com.example.barcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.barcode.utils.ViewPagerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.format.TextStyle;
import java.util.ArrayList;

public class addorg_data extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    RelativeLayout pickimagebtn;
    ViewPager viewPager;
    Uri ImageUri;
    ArrayList<Uri> chooseImageList;
    private MapView mapView;
    private LatLng selectedLocation;
private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorg_data);
        pickimagebtn=findViewById(R.id.chooseImage);
        viewPager = findViewById(R.id.viewPager);
        ImageView add_shops_exit = findViewById(R.id.add_shops_exit);
        Button uploadButton=findViewById(R.id.upload_bt);
        @SuppressLint({"LocalSuppress"}) AppCompatSpinner spinner=findViewById(R.id.spinner_neighbor_unit);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        chooseImageList = new ArrayList<>();
//        List<String> items = Arrays.asList("411","412","413","414");
//        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,items));
//        spinner.setDropDownWidth(300);

        String[] items={"<>","411","412","413","415"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position,convertView,parent );
                if (position==0){
                    ((TextView)view).setText("وحدة الجوار");
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checkpermission();


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
                openInputActivity();
            }
        });

    }

    private void openInputActivity() {
        Dialog inputDialog=new Dialog(this);
        inputDialog.setContentView(R.layout.inpot_billboard);
        inputDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        inputDialog.show();
    }

    public void onMapReady(GoogleMap googleMap) {
      if(location !=null) {

          LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
          googleMap.addMarker(new MarkerOptions().position(latLng).title("marker"));
          googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
          googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f));
      }
        requestLocationPermission();

        // قم بتعيين listener للنقر على الخريطة


    }

    private void Checkpermission() {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(addorg_data.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(addorg_data.this,new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_IMAGE_REQUEST);
            }else{

                pickimageftomgallry();

            }
        }else{

            pickimageftomgallry();

        }
    }

    private void pickimageftomgallry() {



        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            chooseImageList.add(ImageUri);
            SetAdapter();

        }
    }

    private void SetAdapter() {

        ViewPagerAdapter adapter=new ViewPagerAdapter(this,chooseImageList);

        viewPager.setAdapter(adapter);

    }
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            // إذا كان لديك الإذن بالفعل، قم بتحديد الموقع
            setCurrentLocation();
        }
    }

    private void setCurrentLocation() {
        if (selectedLocation != null) {
            // قم بمعالجة الموقع المحدد هنا، يمكنك تخزينه أو استخدامه كما تشاء
            double latitude = selectedLocation.latitude;
            double longitude = selectedLocation.longitude;

            // عرض معلومات الموقع في واجهة المستخدم، أو استخدمها في عمليات أخرى
            Toast.makeText(this, "الموقع المحدد: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // تم منح إذن الوصول إلى الموقع
                mapView.getMapAsync(this);
                setCurrentLocation();
            } else {
                // تم رفض إذن الوصول إلى الموقع
                Toast.makeText(this, "تم رفض إذن الوصول إلى الموقع", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
