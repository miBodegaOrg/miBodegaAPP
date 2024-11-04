package com.mibodega.mystore.shared.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.mibodega.mystore.R;


public class LoadingDialogAdapter {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialogAdapter(Activity myActivity){
        activity = myActivity;
    }

    public LoadingDialogAdapter(){
    }
    public void startLoadingDialog(Activity myactivity, View dialogView, String title, String msj){
        final TextView alertDialogTextView = dialogView.findViewById(R.id.dialog_text);
        final TextView alertDialogTitle = dialogView.findViewById(R.id.dialog_title);
        alertDialogTitle.setText(title);
        alertDialogTextView.setText(msj);

        AlertDialog.Builder builder = new AlertDialog.Builder(myactivity);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialog = builder.create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
    public void dismissDialog(){
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
