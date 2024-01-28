package com.example.barcode;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 111;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    private FloatingActionButton barcodeButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    // إنشاء Intent للانتقال إلى النشاط الجديد
//                    Intent intent = new Intent(this, all_shops_list.class);
//
//                    // وضع البيانات في Intent
//                    intent.putExtra("qrcode", result);
//
//                    // بدء النشاط الجديد
//                    startActivity(intent);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Create a new instance of the SearchOrgFragment and pass the int parameter
                    SearchOrgFragment searchFragment = SearchOrgFragment.newInstance(1);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("qrcode", result);
                    searchFragment.setArguments(bundle2);
                    // Replace the current content with the new fragment
                    fragmentTransaction.replace(android.R.id.content, searchFragment);

                    // Add the transaction to the back stack, allowing the user to navigate back
                    fragmentTransaction.addToBackStack(null);

                    // Commit the transaction to apply the changes
                    fragmentTransaction.commit();

                } else {
                    bundle.getInt(CodeUtils.RESULT_TYPE);
                }
            }
        }
    }
    private void Scan() {
        Intent intent = new Intent(getApplication(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeButton = findViewById(R.id.barcodebtn);
        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    ZXingLibrary.initDisplayOpinion(MainActivity.this);
                    Scan();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
               

            }
        });
        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new profileFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.menuHome);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menulogout:
                        fragment = new setting_profileFragment();
                        break;

//                    case R.id.menuHistory:
//                        fragment = new historyFragment();
//                        break;
//
//                    case R.id.menuSearch:
//                        fragment = new setting_profileFragment();
//                        break;

                    case R.id.menuHome:
                        fragment = new profileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();

                return true;
            }

        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ZXingLibrary.initDisplayOpinion(MainActivity.this);
                Scan();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}