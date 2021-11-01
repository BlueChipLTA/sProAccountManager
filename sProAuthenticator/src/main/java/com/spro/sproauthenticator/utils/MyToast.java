package com.spro.sproauthenticator.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spro.sproauthenticator.R;


public class MyToast {
    private static Toast errorToast = null;
    private static Dialog dialog;

    public static void showErrorMessage(Context context, String message){

        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_toast);

        TextView text = dialog.findViewById(R.id.tvDismiss);
        text.setText(message);

        Button dialogButton = dialog.findViewById(R.id.btDismiss);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    public static void showLongErrorMessage(Context context, String message){

        errorToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        errorToast.setGravity(Gravity.TOP, 0, 150);
        errorToast.show();
        fireLongToast();
    }


    public static void showMessage(Context context, String message){

        errorToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        errorToast.setGravity(Gravity.TOP, 0, 150);
        errorToast.show();
    }

    private static void fireLongToast() {

        Thread t = new Thread() {
            public void run() {
                int count = 0;
                try {
                    while (true && count < 10) {
                        errorToast.show();
                        sleep(1850);
                        count++;
                    }
                } catch (Exception e) {

                }
            }
        };
        t.start();
    }
}
