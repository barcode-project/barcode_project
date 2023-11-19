package com.example.barcode;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class qr_history extends AppCompatActivity {
    private RecyclerView resview;
    private adapter_history adapter_history;
    private List<history> historyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_history);
        resview=findViewById(R.id.history_recyclerView);
        historyList=tisthistory();
        adapter_history=new adapter_history(qr_history.this,historyList);
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