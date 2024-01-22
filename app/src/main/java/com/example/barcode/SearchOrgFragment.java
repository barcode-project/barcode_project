package com.example.barcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcode.Adapter.adpter_shops;
import com.example.barcode.Items.shops;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchOrgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchOrgFragment extends Fragment {
    private RecyclerView recyclerView;
    private com.example.barcode.Adapter.adpter_shops adpter_shops;
    private List<shops> shopsList;
    private ImageView searchExit;
    private SearchView searchView;

    public SearchOrgFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchOrgFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchOrgFragment newInstance(String param1, String param2) {
        SearchOrgFragment fragment = new SearchOrgFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search_org, container, false);
        recyclerView= view.findViewById(R.id.recycle_search);
        searchView= view.findViewById(R.id.etxt_search_org);
        shopsList=generateDummy();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adpter_shops=new adpter_shops(getContext(),shopsList,1);
        recyclerView.setAdapter(adpter_shops);
        return view;
    }
    private List<shops> generateDummy() {
        List<shops> shoplist =new ArrayList<>();
        shoplist.add(new shops("hhh","1","xx",1,"khjk"));

        return shoplist;
    }
}