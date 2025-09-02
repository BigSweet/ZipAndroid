package com.zip.zipandroid.utils.phonedate;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.zip.zipandroid.utils.phonedate.applist.InstalledAppListener;
import com.zip.zipandroid.utils.phonedate.applist.InstalledAppsProvider;
import com.zip.zipandroid.utils.phonedate.calendar.CalendarListener;
import com.zip.zipandroid.utils.phonedate.calendar.CalendarProvider;
import com.zip.zipandroid.utils.phonedate.calllog.CallLogListener;
import com.zip.zipandroid.utils.phonedate.calllog.CallLogsProvider;
import com.zip.zipandroid.utils.phonedate.contact.ContactListener;
import com.zip.zipandroid.utils.phonedate.contact.ContactsProvider;
import com.zip.zipandroid.utils.phonedate.location.LocationListener;
import com.zip.zipandroid.utils.phonedate.location.LocationProvider;
import com.zip.zipandroid.utils.phonedate.location.device.DeviceInfoUtil;
import com.zip.zipandroid.utils.phonedate.sms.SMSMessageListener;
import com.zip.zipandroid.utils.phonedate.sms.SMSMessagesProvider;

public class PhoneDateProvider {
    private Context context;

    private ContactsProvider contactsProvider;
    private InstalledAppsProvider installedAppsProvider;
    private SMSMessagesProvider smsMessagesProvider;
    private CallLogsProvider callLogsProvider;
    private LocationProvider locationProvider;
    private static PhoneDateProvider phoneDateProvider;
    private CalendarProvider calendarProvider;

    public static PhoneDateProvider sharedInstance(Application application) {
        if (phoneDateProvider == null) {
            synchronized (PhoneDateProvider.class) {
                phoneDateProvider = new PhoneDateProvider(application);
            }
        }
        return phoneDateProvider;
    }

    private PhoneDateProvider(Application application) {
        this.context = application.getApplicationContext();
    }

    /**
     * 获取设备信息
     */
    public DeviceInfoUtil getDeviceInfo() {
        return new DeviceInfoUtil(context);
    }

    /**
     * 获取通讯录
     *
     * @param listener
     */
    public void getContacts(@Nullable final ContactListener listener) {
        if (this.contactsProvider == null) {
            synchronized (this) {
                if (this.contactsProvider == null) {
                    this.contactsProvider =
                            new ContactsProvider(this.context);
                }
            }
        }
        this.contactsProvider.fetchAllContacts(new ContactListener[]{listener});
    }

    /**
     * 获取定位信息
     *
     * @param listener
     */
    public void getLocation(LocationListener listener) {
        if (this.locationProvider == null) {
            synchronized (this) {
                if (this.locationProvider == null) {
                    this.locationProvider = new LocationProvider(this.context);
                }
            }
        }
        this.locationProvider.fetchLocation(new LocationListener[]{listener});
    }

    /**
     * 获取手机第三方app列表
     *
     * @param listener
     */
    public void getInstalledApps(InstalledAppListener listener) {
        if (this.installedAppsProvider == null) {
            synchronized (this) {
                if (this.installedAppsProvider == null) {
                    this.installedAppsProvider =
                            new InstalledAppsProvider(this.context);
                }
            }
        }
        this.installedAppsProvider.fetchInstalledApps(new InstalledAppListener[]{listener});
    }

    /**
     * 获取短信
     *
     * @param listener
     */
    public void getSMSMessages(SMSMessageListener listener) {
        if (this.smsMessagesProvider == null) {
            synchronized (this) {
                if (this.smsMessagesProvider == null) {
                    this.smsMessagesProvider =
                            new SMSMessagesProvider(this.context);
                }
            }
        }
        this.smsMessagesProvider.fetchSMSMessages(new SMSMessageListener[]{listener});
    }

    /**
     * 获取通话记录
     *
     * @param listener
     */
    public void getCallLogs(CallLogListener listener) {
        if (this.callLogsProvider == null) {
            synchronized (this) {
                if (this.callLogsProvider == null) {
                    this.callLogsProvider =
                            new CallLogsProvider(this.context);
                }
            }
        }
        this.callLogsProvider.fetchCallLogs(new CallLogListener[]{listener});
    }

    /**
     * 获取手机日历数据
     *
     * @param listener
     */
    public void getCalendarInfo(CalendarListener listener) {
        if (this.calendarProvider == null) {
            synchronized (this) {
                if (this.calendarProvider == null) {
                    this.calendarProvider =
                            new CalendarProvider(this.context);
                }
            }
        }
        this.calendarProvider.fetchCalendar(new CalendarListener[]{listener});
    }
}
