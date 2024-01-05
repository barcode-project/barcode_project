package com.example.barcode.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcode.R;
import com.example.barcode.Items.history;
import com.example.barcode.org_details;

import java.util.List;

public class adapter_history extends RecyclerView.Adapter<adapter_history.historyViewHolder> {

    private List<history> historyList;
    private Context context;

    public adapter_history(Context context, List<history> historyList) {
        this.context = context;
        this.historyList = historyList;
    }


    public void setFlteredList(List<history> filFlteredList) {
        this.historyList = filFlteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_items, parent, false);
        return new historyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull historyViewHolder holder, int position) {
        history history=historyList.get(position);
        holder.time.setText(history.getTime());
        holder.history_name.setText(history.getHistory_name());
        holder.id_nu.setText(String.valueOf(history.getId_nu()));

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class historyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time, history_name, id_nu;
        CardView cv;

        public historyViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.list_item1_root_cardView);
            time = itemView.findViewById(R.id.notificationtime);
            history_name = itemView.findViewById(R.id.history_item_name);
            id_nu = itemView.findViewById(R.id.id_number);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, org_details.class);
            intent.putExtra("id_nu", history.getId_nu());
            context.startActivity(intent);

        }
    }
}
