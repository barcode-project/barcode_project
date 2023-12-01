package com.example.barcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.barcode.utils.ViewPagerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class addorg_data extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    TextInputEditText address_unit,doors_numbers;
    TextInputLayout doorss_numbers;

    RelativeLayout pickimagebtn;
    ViewPager viewPager;
    Uri ImageUri;
    ArrayAdapter<String> streetAdapter;
    ArrayList<Uri> chooseImageList;
    List<String> name_street;
    String selectedstreetID;

    private LatLng currentLatLng;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorg_data);
        pickimagebtn=findViewById(R.id.chooseImage);
        viewPager = findViewById(R.id.viewPager);
        ImageView add_shops_exit = findViewById(R.id.add_shops_exit);
        Button uploadButton=findViewById(R.id.upload_bt);
        address_unit=findViewById(R.id.address_unit);
        doors_numbers=findViewById(R.id.doors_numbers);
        @SuppressLint({"LocalSuppress"}) AppCompatSpinner spinner=findViewById(R.id.spinner_neighbor_unit);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        chooseImageList = new ArrayList<>();

         name_street= new ArrayList<>();



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
        doors_numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(addorg_data.this);
                builder.setTitle("اختار المطلوب");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String option = adapter.getItem(which);
                        doors_numbers.setText(option);

                    }
                });
                builder.show();
            }

        });
        address_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streetAdapter= new ArrayAdapter<String>(addorg_data.this, android.R.layout.simple_list_item_1);
                streetAdapter.addAll(name_street);

                AlertDialog.Builder dialog = new AlertDialog.Builder(addorg_data.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialog_button = dialogView.findViewById(R.id.dialog_button);
                EditText dialog_input = dialogView.findViewById(R.id.dialog_input);
                TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);


//                dialog_title.setText("");
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

                        String street_id = "0";
                        address_unit.setText(selectedItem);


                        for (int i = 0; i < name_street.size(); i++) {
                            if (name_street.get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
//                                street_id = street.get(i).get("street_id");
                            }
                        }


                        selectedstreetID = street_id;
                        Log.d("category_id", street_id);
                    }
                });
            }

        });

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

    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            requestLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
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
    private void requestLocationUpdates() {
        if (googleMap != null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
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
            double latitude = currentLatLng.latitude;
            double longitude = currentLatLng.longitude;

            String locationText = "Latitude: " + latitude + "\nLongitude: " + longitude;
//            address_unit.setText(locationText);
            // عرض معلومات الموقع في واجهة المستخدم، أو استخدمها في عمليات أخرى
            Toast.makeText(this, "الموقع المحدد: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                // Permission granted, request location updates
                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                    requestLocationUpdates();
                }
            }
        }
    }
}
