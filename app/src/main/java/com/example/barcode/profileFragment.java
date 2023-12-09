package com.example.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class profileFragment extends Fragment {
    private CardView show_shops_card,add_shop_card,history_shops_card,show_added_shops_cardview;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }


    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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
       View view= inflater.inflate(R.layout.fragment_profile, container, false);
       show_shops_card=view.findViewById(R.id.show_all_shops_cardview);
       show_shops_card.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent inten=new Intent(getActivity(),all_shops_list.class);
               startActivity(inten);
           }
       });
       add_shop_card=view.findViewById(R.id.add_shops_cardview);
       add_shop_card.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent inte=new Intent(getActivity(),addorg_data.class);
               startActivity(inte);
           }
       });
       history_shops_card=view.findViewById(R.id.history_shops_cardview);
       history_shops_card.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent inte=new Intent(getActivity(),qr_history.class);
               startActivity(inte);
           }
       });
        show_added_shops_cardview=view.findViewById(R.id.show_added_shops_cardview);
        show_added_shops_cardview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent inte=new Intent(getActivity(),AddedOrgsList.class);
               startActivity(inte);
           }
       });
        return view;
    }



}