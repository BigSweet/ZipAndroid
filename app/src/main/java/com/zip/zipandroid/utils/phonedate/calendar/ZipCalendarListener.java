package com.zip.zipandroid.utils.phonedate.calendar;

public interface ZipCalendarListener {
    void onCalendarFetched(ZipCalendarInfos[] calendarInfo);
    void onError(String message);
}
