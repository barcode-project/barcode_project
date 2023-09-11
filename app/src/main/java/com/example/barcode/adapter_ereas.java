package com.example.barcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapter_ereas extends RecyclerView.Adapter<adapter_ereas.eareasViewHolder> {
    private Context context;
    private List<ereas> ereasList;

    public adapter_ereas(Context context, List<ereas> ereasList) {
        this.context = context;
        this.ereasList = ereasList;
    }
    @NonNull
    @Override
    public adapter_ereas.eareasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.all_ereas, parent, false);
        return new eareasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_ereas.eareasViewHolder holder, int position) {
        ereas erea =ereasList.get(position);
        holder.name_eares.setText(erea.getName_ereas());

    }

    @Override
    public int getItemCount() {
       return ereasList.size();
    }

    public class eareasViewHolder extends RecyclerView.ViewHolder {
        TextView name_eares;
        public eareasViewHolder(@NonNull View itemView) {
            super(itemView);
            name_eares = itemView.findViewById(R.id.list_item1_title_name);
        }

    }
}
