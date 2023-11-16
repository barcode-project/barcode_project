package com.example.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 111;
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
                    homeFragment  fragament = new homeFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("qrcode", result);
                    fragament.setArguments(bundle2);

                    bottomNavigationView = findViewById(R.id.bottom_nav);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragament).commit();
                    bottomNavigationView.setSelectedItemId(R.id.menuHome);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

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
                ZXingLibrary.initDisplayOpinion(MainActivity.this);
                Scan();

            }
        });
        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new homeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.menuHome);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menulogout:
                        fragment = new profileFragment();
                        break;

                    case R.id.menuHistory:
                        fragment = new historyFragment();
                        break;

                    case R.id.menuSearch:
                        fragment = new searchFragment();
                        break;

                    case R.id.menuHome:
                        fragment = new homeFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();

                return true;
            }

        });


    }
}