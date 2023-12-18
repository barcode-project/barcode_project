package com.example.barcode;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class ProgressDialogBuilder {

    private AlertDialog dialog;

    public ProgressDialogBuilder(Context context) {
        // Create a dialog with a custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(false); // Optional: Set if the dialog can be canceled

        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}

