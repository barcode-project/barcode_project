package com.example.barcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.barcode.utils.ViewPagerAdapter;

import java.util.ArrayList;

public class addorg_data extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    RelativeLayout pickimagebtn;
    ViewPager viewPager;
    Uri ImageUri;
    ArrayList<Uri> chooseImageList;
    private ImageView add_shops_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorg_data);
        pickimagebtn=findViewById(R.id.chooseImage);
        viewPager = findViewById(R.id.viewPager);
        add_shops_exit=findViewById(R.id.add_shops_exit);
        chooseImageList = new ArrayList<>();

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

        //here we go to gallery to select images
//        Intent intent =new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,1);

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
}