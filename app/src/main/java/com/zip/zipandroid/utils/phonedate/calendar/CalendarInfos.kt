package com.zip.zipandroid.utils.phonedate.calendar

import java.io.Serializable


class CalendarInfos : Serializable{
    var title: String? = null
    var description: String? = null
    var eventLocation: String? = null
    var dtstart: String? = null
    var dtend: String? = null
    var week: String? = null
}