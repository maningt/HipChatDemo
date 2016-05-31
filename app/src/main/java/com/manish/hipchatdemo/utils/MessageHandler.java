package com.manish.hipchatdemo.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.manish.hipchatdemo.R;


/**
 * Created by manishdewan on 26/05/16.
 */

/**
 * Message Handler class to show Dialogs and Toast in Application
 */
public class MessageHandler {
    private static MessageHandler mMessageHandler;
    private ProgressDialog mProgressDialog;

    /**
     * private Constructor of Singleton Class
     */
    private MessageHandler() {

    }

    /**
     * @return method to get Single Instance of this class
     */
    public static MessageHandler getInstance() {
        if (mMessageHandler == null) {
            mMessageHandler = new MessageHandler();
        }
        return mMessageHandler;
    }


    /**
     * @param context
     * @param message
     * @return method to show Toast message
     */
    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @return method to initialize the ProgressDialog
     */
    public void createProgresDialog(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.loading_text));
    }

    /**
     * method to show Progress Dialog
     */
    public void showProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.show();
    }

    /**
     * method to hide Progress Dialog
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}
