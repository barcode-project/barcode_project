package com.example.barcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.barcode.utils.ViewPagerAdapter;

import java.util.ArrayList;

public class addorg_data extends AppCompatActivity {

    RelativeLayout pickimagebtn;
    ViewPager viewPager;
    Uri ImageUri;
    ArrayList<Uri> chooseImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorg_data);
        pickimagebtn=findViewById(R.id.chooseImage);
        viewPager = findViewById(R.id.viewPager);
        chooseImageList = new ArrayList<>();

        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checkpermission();

                pickimageftomgallry();
            }
        });

    }

    private void Checkpermission() {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(addorg_data.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(addorg_data.this,new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
            }else{

                pickimageftomgallry();

            }
        }else{

            pickimageftomgallry();

        }
    }

    private void pickimageftomgallry() {

        //here we go to gallery to select images
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && requestCode==RESULT_OK && data!=null && data.getClipData()!=null){
            int count=data.getClipData().getItemCount();
            for (int i=0 ; i<count;i++){
                ImageUri=data.getClipData().getItemAt(i).getUri();

                //add list that add all image uri
                chooseImageList.add(ImageUri);
                SetAdapter();
            }
        }

    }

    private void SetAdapter() {

        ViewPagerAdapter adapter=new ViewPagerAdapter(this,chooseImageList);
        viewPager.setAdapter(adapter);

    }
}