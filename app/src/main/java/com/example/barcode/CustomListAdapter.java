package com.example.barcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<ListItem> {

    public CustomListAdapter(Context context, List<ListItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.billboard_item, parent, false);
        }

        // Lookup view for data population
        TextView textName = convertView.findViewById(R.id.textName);
        TextView textAgeSex = convertView.findViewById(R.id.textAgeSex);

        // Populate the data into the template view using the data object
        if (item != null) {
            textName.setText(item.getName());
            textAgeSex.setText(item.getAgeSex());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}

