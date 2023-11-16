package com.example.barcode;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class homeFragment extends Fragment {
    private RecyclerView ReView;
    private EditText etxt_search;
    private adapter_ereas adapter_ereas;
    private List<ereas> ereasList;

    private static final String ARG_PARAM1 = "param1";
    private static final String Qrcode = "param2";

    private String mParam1;
    private String data="";
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }


    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(Qrcode, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(Qrcode);
            data = getArguments().getString("qrcode");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        ReView= view.findViewById(R.id.a_d_c_recyclerView);
        etxt_search= view.findViewById(R.id.etxt_search_home);
etxt_search.clearComposingText();
        etxt_search.setText(data);
        etxt_search.setText(data);
        ereasList=generateDummy();
        Log.d("qrcode",data);
        Log.d(Qrcode,data);
        adapter_ereas=new adapter_ereas(getContext(),ereasList);
        ReView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ReView.setAdapter(adapter_ereas);
        return view;

    }

    private List<ereas> generateDummy() {
        List<ereas> ereas =new ArrayList<>();
        ereas.add(new ereas("الحي السياسي"));
        ereas.add(new ereas("البليبي"));
        ereas.add(new ereas("الزبيري"));
        ereas.add(new ereas("حده"));
        ereas.add(new ereas("الحي السياسي"));
        ereas.add(new ereas("البليبي"));
        ereas.add(new ereas("الزبيري"));
        ereas.add(new ereas("حده"));
        ereas.add(new ereas("الحي السياسي"));
        ereas.add(new ereas("البليبي"));
        ereas.add(new ereas("الزبيري"));
        ereas.add(new ereas("حده"));
        return ereas;
    }
}