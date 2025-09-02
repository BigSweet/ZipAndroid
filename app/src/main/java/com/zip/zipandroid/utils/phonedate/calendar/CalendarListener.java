package com.zip.zipandroid.utils.phonedate.calendar;

public interface CalendarListener {
    void onCalendarFetched(CalendarInfos[] calendarInfo);
    void onError(String message);
}
