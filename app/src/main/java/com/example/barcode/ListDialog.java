package com.example.barcode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.barcode.Adapter.CustomListAdapter;

import java.util.List;

public class ListDialog {

    public static void showListDialog(Context context, String title, List<PlateData> items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the dialog title
        builder.setTitle(title);

        // Create a custom adapter
        CustomListAdapter adapter = new CustomListAdapter(context, items);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Handle item click here if needed
                // For example, you can get the selected item: items.get(which)
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
