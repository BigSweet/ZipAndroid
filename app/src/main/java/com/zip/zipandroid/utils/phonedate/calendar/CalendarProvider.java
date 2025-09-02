package com.zip.zipandroid.utils.phonedate.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarProvider {
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private Context context;
    private ArrayList<CalendarListener> listeners;
    private boolean isFetching = false;

    public CalendarProvider(Context context) {
        this.context = context;
        listeners = new ArrayList<>();
    }

    public void fetchCalendar(CalendarListener[] listeners) {
        synchronized (this) {
            for (CalendarListener listener : listeners) {
                if (!this.listeners.contains(listener)) {
                    this.listeners.add(listener);
                }
            }
            if (this.isFetching) {
                return;
            }
            this.isFetching = true;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                CalendarInfos[] calendarInfo = new CalendarInfos[0];
                try {
                    calendarInfo = CalendarProvider.this.fetchCalendar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CalendarProvider.this.onFinishedfetchCalendar(calendarInfo);
            }
        }).start();
    }


    @SuppressLint("Range")
    private CalendarInfos[] fetchCalendar() {
        ArrayList<CalendarInfos> calendarInfo=new ArrayList<CalendarInfos>();
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null,
                null, null,  "dtstart"+" DESC");
        while (eventCursor.moveToNext()){
            CalendarInfos bean = new CalendarInfos();
            bean.setTitle(eventCursor.getString(eventCursor.getColumnIndex("title")));
            bean.setDescription(eventCursor.getString(eventCursor.getColumnIndex("description")));
            bean.setEventLocation(eventCursor.getString(eventCursor.getColumnIndex("eventLocation")));
            if(eventCursor.getString(eventCursor.getColumnIndex("dtstart"))!=null){
                String startTime = timeStamp2Date(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtstart"))));
                bean.setDtstart(startTime);
                bean.setWeek(""+ (getWeek(startTime)));
            }

            if(eventCursor.getString(eventCursor.getColumnIndex("dtend"))!=null){
                bean.setDtend(timeStamp2Date(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtend")))));
            }
            calendarInfo.add(bean);
        }
        return calendarInfo.toArray(new CalendarInfos[]{});
    }

    private void onFinishedfetchCalendar(CalendarInfos[] infos) {
        ArrayList<CalendarListener> listenerList;
        synchronized (this) {
            listenerList = this.listeners;
            this.listeners = new ArrayList<>();
            this.isFetching = false;
        }

        if (listenerList != null && listenerList.size() > 0) {
            for (int i = 0; i < listenerList.size(); i++) {
                listenerList.get(i).onCalendarFetched(infos);
            }
        }
    }

    /**
     * 时间戳转换为字符串
     * @param time:时间戳
     * @return
     */
    private static String timeStamp2Date(long time) {
        if(time==-1){
            return "";
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    private static int getWeek(String pTime) {
        int Week = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week = 0;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week = 1;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week = 2;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week = 3;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week = 4;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week = 5;
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week = 6;
        }

        return Week;
    }
}
