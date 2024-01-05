package com.example.barcode.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcode.AddedShopDetails;
import com.example.barcode.R;
import com.example.barcode.Items.shops;
import com.example.barcode.shops_details;

import java.util.List;

public class adpter_shops extends RecyclerView.Adapter<adpter_shops.shopsViewHolder> {
    private  Context context;
    private List<shops> shopsList;
    private int flag;
    public adpter_shops(Context context, List<shops> shopsList,int flag) {
        this.context = context;
        this.shopsList = shopsList;
        this.flag = flag;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setFlteredList(List<shops>filFlteredList){
        this.shopsList=filFlteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public shopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.history_items, parent, false);
        return new shopsViewHolder(view);
    }

       @Override
    public void onBindViewHolder(@NonNull shopsViewHolder holder, int position) {
        shops shop =shopsList.get(position);
        holder.name_shops.setText(shop.getName_shop());
        holder.owner_name.setText(shop.getOwner_name());
        holder.namefullname.setText( shop.getOwner_namefullname());
        if(flag == 2) {
            if (shop.getStatus().equals("0")) {
                holder.status.setText("لم تنقل");
            } else {
                holder.status.setText("تم النقل");
            }
        }
        else if (flag == 1){
            holder.status.setText(shop.getStatus());
        }
        holder.id_nu.setText(String.valueOf(shop.getId()));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(flag == 1) {
                    intent = new Intent(context, shops_details.class);
                }
                else if (flag == 2){
                    intent = new Intent(context, AddedShopDetails.class);
                }
                intent.putExtra("id", shop.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    public class shopsViewHolder extends RecyclerView.ViewHolder {
        TextView name_shops, status, id_nu, owner_name, namefullname;
        CardView cv;

        public shopsViewHolder(@NonNull View itemView) {
            super(itemView);
            name_shops = itemView.findViewById(R.id.history_item_name);
            status = itemView.findViewById(R.id.notificationtime);
            owner_name = itemView.findViewById(R.id.history_winer_name);
            id_nu = itemView.findViewById(R.id.id_number);
            namefullname = itemView.findViewById(R.id.history_winer_nam);
            cv = itemView.findViewById(R.id.list_item1_root_cardView);

        }
    }
}
