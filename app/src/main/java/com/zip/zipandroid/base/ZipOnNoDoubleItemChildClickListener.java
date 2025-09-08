package com.zip.zipandroid.base;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Calendar;

public abstract class ZipOnNoDoubleItemChildClickListener implements
        BaseQuickAdapter.OnItemChildClickListener {

    public static int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    public ZipOnNoDoubleItemChildClickListener(int MIN_CLICK_DELAY_TIME) {
        ZipOnNoDoubleItemChildClickListener.MIN_CLICK_DELAY_TIME = MIN_CLICK_DELAY_TIME;
    }

    public ZipOnNoDoubleItemChildClickListener() {
        MIN_CLICK_DELAY_TIME = 500;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            OnNoDoubleItemChildClick(adapter, view, position);
        }
    }

    public abstract void OnNoDoubleItemChildClick(BaseQuickAdapter adapter, View view, int position);

}
