package com.example.barcode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adpter_shops extends RecyclerView.Adapter<adpter_shops.shopsViewHolder> {
    private Context context;
    private List<shops> shopsList;

    public adpter_shops(Context context, List<shops> shopsList) {
        this.context = context;
        this.shopsList = shopsList;
    }
    @NonNull
    @Override
    public shopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_shops, parent, false);
        return new shopsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull shopsViewHolder holder, int position) {
        shops shop =shopsList.get(position);
        holder.name_shops.setText(shop.getName_shop());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, shops_details.class) ;
                intent.putExtra("id", shops.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    public class shopsViewHolder extends RecyclerView.ViewHolder {
        TextView name_shops;
        CardView cv;
        public shopsViewHolder(@NonNull View itemView) {
            super(itemView);
            name_shops= itemView.findViewById(R.id.list_item1_title_name2);

        }
    }
}
