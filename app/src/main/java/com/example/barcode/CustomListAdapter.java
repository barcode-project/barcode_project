package com.example.barcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<PlateData> {

    public CustomListAdapter(Context context, List<PlateData> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PlateData item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.billboard_item, parent, false);
        }

        // Lookup view for data population
        TextView type = convertView.findViewById(R.id.type);
        TextView length = convertView.findViewById(R.id.length);
        TextView width = convertView.findViewById(R.id.width);
        TextView quan = convertView.findViewById(R.id.quan);

        // Populate the data into the template view using the data object
        if (item != null) {
            type.setText(item.getPlateType());
            length.setText(String.valueOf(item.getLength()));
            width.setText(String.valueOf(item.getWidth()));
            quan.setText(String.valueOf(item.getQuantity()));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}

