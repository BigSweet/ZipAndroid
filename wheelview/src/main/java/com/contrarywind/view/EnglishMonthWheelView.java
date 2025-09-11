package com.contrarywind.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.contrarywind.interfaces.IPickerViewData;
import com.contrarywind.view.WheelView;

public class EnglishMonthWheelView extends WheelView {
    public EnglishMonthWheelView(Context context) {
        super(context);
    }

    public EnglishMonthWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String getContentText(Object item) {
        if (item == null || TextUtils.isEmpty(item.toString())) {
            return "";
        } else if (item instanceof IPickerViewData) {
            return ((IPickerViewData) item).getPickerViewText();
        } else if (item instanceof Integer) {
            //如果为整形则最少保留两位数.
            int value = (int) item;
            String month = null;
            switch (value) {
                case 1:
                    month = "Jan";
                    break;
                case 2:
                    month = "Feb";
                    break;
                case 3:
                    month = "Mar";
                    break;
                case 4:
                    month = "Apr";
                    break;
                case 5:
                    month = "May";
                    break;
                case 6:
                    month = "Jun";
                    break;
                case 7:
                    month = "Jul";
                    break;
                case 8:
                    month = "Aug";
                    break;
                case 9:
                    month = "Sep";
                    break;
                case 10:
                    month = "Oct";
                    break;
                case 11:
                    month = "Nov";
                    break;
                case 12:
                    month = "Dec";
                    break;
            }
            if (month == null) {
                Log.d("日期选择器", value + "");
                return value + "";
            } else {
                Log.d("日期选择器", month.toString());
                return month;
            }

        }
        return item.toString();
    }
}
