package com.aecrm.Models;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import java.net.URL;

public class APIHeaderModel {

     String NAMESPACE = "http://tempuri.org/";
     String URL = "http://203.134.206.216:85/aeservice.asmx";
     String TAG = "AE CRM";


     public  APIHeaderModel(){}

    public String getURL() {
        return URL;
    }

    public String getTAG() {
        return TAG;
    }

    public String getNAMESPACE() {
        return NAMESPACE;
    }


    //Alert DialogBox
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        // Setting OK Button

        // Setting OK Button

        // Showing Alert Message
        alertDialog.show();
    }


}
