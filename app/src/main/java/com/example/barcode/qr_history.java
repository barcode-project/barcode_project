package com.example.barcode;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class qr_history extends AppCompatActivity {
    private adapter_history adapter_history;
    private List<history> historyList;
    EditText historysearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_history);
        RecyclerView resview = findViewById(R.id.history_recyclerView);
        historysearch = findViewById(R.id.history_search);
        historysearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


                //  searchData(s.toString());

                //get data from local database

                List<history> filterList = new ArrayList<>();
                if (historyList != null) {
                    for (history shop : historyList) {
                        if (shop.getHistory_name().toLowerCase().contains(charSequence.toString())) {
                            filterList.add(shop);
                        }
                    }
                    if (filterList.isEmpty()) {

                        Toast.makeText(qr_history.this, " لا يوجد منشأه بهاذا الاسم", Toast.LENGTH_LONG).show();

//                    recyclerView.setVisibility(View.GONE);
//                    imgNoProduct.setVisibility(View.VISIBLE);
//                    imgNoProduct.setImageResource(R.drawable.no_data);


                    } else {

                        if (adapter_history != null) {
                            adapter_history.setFlteredList(filterList);
                        } else {
                            Log.e("Adapter Error", "adpter_shops is null");
                        }
                    }
                } else {
                    Log.e("List Error", "shopsList is null");
                    Toast.makeText(qr_history.this, "قائمة المحلات فارغة", Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        historyList = tisthistory();
        adapter_history = new adapter_history(qr_history.this, historyList);
        resview.setLayoutManager(new LinearLayoutManager(qr_history.this));
        resview.setAdapter(adapter_history);

    }


    private List<history> tisthistory() {
        List<history> history= new ArrayList<>();
        history.add(new history(" قبل 5دقائق","شارع مجاهد",1));
        history.add(new history(" قبل 10دقائق","شارع عمان",1));
        history.add(new history(" قبل 15دقائق","شارع صفر",1));
        history.add(new history(" قبل 17دقائق","شارع ايران",1));
        history.add(new history(" قبل 21دقائق","شارع مجاهد",1));
        history.add(new history(" قبل 55دقائق","شارع حدة",1));
        history.add(new history(" قبل 5دقائق","شارع مجاهد",1));
        history.add(new history(" قبل 10دقائق","شارع عمان",1));
        history.add(new history(" قبل 15دقائق","شارع صفر",1));
        history.add(new history(" قبل 17دقائق","شارع ايران",1));
        history.add(new history(" قبل 21دقائق","شارع مجاهد",1));
        history.add(new history(" قبل 55دقائق","شارع حدة",1));

        return history;
    }

}