package com.zip.zipandroid.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.zip.zipandroid.R;


public class ZipProgressDialog extends ProgressDialog {
    private Context mContext;

    public ZipProgressDialog(Context context) {
        super(context, R.style.Dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.zip_loading_view);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

