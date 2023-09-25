package com.example.barcode;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class historyFragment extends Fragment {
private RecyclerView resview;
private adapter_history adapter_history;
private List<history> historyList;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public historyFragment() {
        // Required empty public constructor
    }


    public static historyFragment newInstance(String param1, String param2) {
        historyFragment fragment = new historyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        resview=view.findViewById(R.id.history_recyclerView);
        historyList=tisthistory();
        adapter_history=new adapter_history(getContext(),historyList);
        resview.setLayoutManager(new LinearLayoutManager(getActivity()));
        resview.setAdapter(adapter_history);

        return view;
    }

    private List<history> tisthistory() {
        List<history> history= new ArrayList<>();
        history.add(new history(" قبل 5دقائق","شارع مجاهد","1"));
        history.add(new history(" قبل 10دقائق","شارع عمان","2"));
        history.add(new history(" قبل 15دقائق","شارع صفر","3"));
        history.add(new history(" قبل 17دقائق","شارع ايران","4"));
        history.add(new history(" قبل 21دقائق","شارع مجاهد","5"));
        history.add(new history(" قبل 55دقائق","شارع حدة","6"));
        history.add(new history(" قبل 57دقائق","شارع عمان","7"));
        history.add(new history(" قبل 59دقائق","شارع مجاهد","8"));
        return history;
    }
}