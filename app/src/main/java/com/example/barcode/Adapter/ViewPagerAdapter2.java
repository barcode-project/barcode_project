package com.example.barcode.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.barcode.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerAdapter2 extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageUrls;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter2(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.layoutInflater = LayoutInflater.from(context);
        Log.d("fullImageUrl",this.imageUrls.toString());
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.showimageslayout, container, false);
        ImageView imageView = view.findViewById(R.id.ImageUpload);



        // Prepend base URL to the image URL
//        String fullImageUrl = "https://demo.qryemen.com/" + imageUrls.get(position);

        // Use Picasso to load and display the image
        try {
            Picasso.get().load(imageUrls.get(position)).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    // Image loaded successfully
                }

                @Override
                public void onError(Exception e) {
                    // Handle error, load placeholder image or show a toast
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that might occur during image loading
        }

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}


