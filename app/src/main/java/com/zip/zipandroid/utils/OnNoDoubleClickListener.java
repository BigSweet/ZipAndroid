package com.zip.zipandroid.utils;

import android.view.View;

import java.util.Calendar;

public abstract class OnNoDoubleClickListener implements View.OnClickListener {

    public int MIN_CLICK_DELAY_TIME = 300;
    private long lastClickTime = 0;

    public OnNoDoubleClickListener(int MIN_CLICK_DELAY_TIME) {
        this.MIN_CLICK_DELAY_TIME = MIN_CLICK_DELAY_TIME;
    }

    public OnNoDoubleClickListener() {
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);

}
