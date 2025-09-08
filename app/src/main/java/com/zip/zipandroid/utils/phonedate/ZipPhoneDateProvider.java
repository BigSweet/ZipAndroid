package com.zip.zipandroid.utils.phonedate;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.zip.zipandroid.utils.phonedate.applist.ZipInstalledAppListener;
import com.zip.zipandroid.utils.phonedate.applist.ZipInstalledAppsProvider;
import com.zip.zipandroid.utils.phonedate.calendar.ZipCalendarListener;
import com.zip.zipandroid.utils.phonedate.calendar.ZipCalendarProvider;
import com.zip.zipandroid.utils.phonedate.calllog.ZipCallLogListener;
import com.zip.zipandroid.utils.phonedate.calllog.ZipCallLogsProvider;
import com.zip.zipandroid.utils.phonedate.contact.ZipContactListener;
import com.zip.zipandroid.utils.phonedate.contact.ZipContactsProvider;
import com.zip.zipandroid.utils.phonedate.location.ZipLocationListener;
import com.zip.zipandroid.utils.phonedate.location.ZipLocationProvider;
import com.zip.zipandroid.utils.phonedate.location.device.ZipDeviceInfoUtil;
import com.zip.zipandroid.utils.phonedate.sms.ZipSMSMessageListener;
import com.zip.zipandroid.utils.phonedate.sms.ZipSMSMessagesProvider;

public class ZipPhoneDateProvider {
    private Context context;

    private ZipContactsProvider zipContactsProvider;
    private ZipInstalledAppsProvider zipInstalledAppsProvider;
    private ZipSMSMessagesProvider zipSmsMessagesProvider;
    private ZipCallLogsProvider zipCallLogsProvider;
    private ZipLocationProvider zipLocationProvider;
    private static ZipPhoneDateProvider zipPhoneDateProvider;
    private ZipCalendarProvider zipCalendarProvider;

    public static ZipPhoneDateProvider sharedInstance(Application application) {
        if (zipPhoneDateProvider == null) {
            synchronized (ZipPhoneDateProvider.class) {
                zipPhoneDateProvider = new ZipPhoneDateProvider(application);
            }
        }
        return zipPhoneDateProvider;
    }

    private ZipPhoneDateProvider(Application application) {
        this.context = application.getApplicationContext();
    }

    /**
     * 获取设备信息
     */
    public ZipDeviceInfoUtil getDeviceInfo() {
        return new ZipDeviceInfoUtil(context);
    }

    /**
     * 获取通讯录
     *
     * @param listener
     */
    public void getContacts(@Nullable final ZipContactListener listener) {
        if (this.zipContactsProvider == null) {
            synchronized (this) {
                if (this.zipContactsProvider == null) {
                    this.zipContactsProvider =
                            new ZipContactsProvider(this.context);
                }
            }
        }
        this.zipContactsProvider.fetchAllContacts(new ZipContactListener[]{listener});
    }

    /**
     * 获取定位信息
     *
     * @param listener
     */
    public void getLocation(ZipLocationListener listener) {
        if (this.zipLocationProvider == null) {
            synchronized (this) {
                if (this.zipLocationProvider == null) {
                    this.zipLocationProvider = new ZipLocationProvider(this.context);
                }
            }
        }
        this.zipLocationProvider.fetchLocation(new ZipLocationListener[]{listener});
    }

    /**
     * 获取手机第三方app列表
     *
     * @param listener
     */
    public void getInstalledApps(ZipInstalledAppListener listener) {
        if (this.zipInstalledAppsProvider == null) {
            synchronized (this) {
                if (this.zipInstalledAppsProvider == null) {
                    this.zipInstalledAppsProvider =
                            new ZipInstalledAppsProvider(this.context);
                }
            }
        }
        this.zipInstalledAppsProvider.fetchInstalledApps(new ZipInstalledAppListener[]{listener});
    }

    /**
     * 获取短信
     *
     * @param listener
     */
    public void getSMSMessages(ZipSMSMessageListener listener) {
        if (this.zipSmsMessagesProvider == null) {
            synchronized (this) {
                if (this.zipSmsMessagesProvider == null) {
                    this.zipSmsMessagesProvider =
                            new ZipSMSMessagesProvider(this.context);
                }
            }
        }
        this.zipSmsMessagesProvider.fetchSMSMessages(new ZipSMSMessageListener[]{listener});
    }

    /**
     * 获取通话记录
     *
     * @param listener
     */
    public void getCallLogs(ZipCallLogListener listener) {
        if (this.zipCallLogsProvider == null) {
            synchronized (this) {
                if (this.zipCallLogsProvider == null) {
                    this.zipCallLogsProvider =
                            new ZipCallLogsProvider(this.context);
                }
            }
        }
        this.zipCallLogsProvider.fetchCallLogs(new ZipCallLogListener[]{listener});
    }

    /**
     * 获取手机日历数据
     *
     * @param listener
     */
    public void getCalendarInfo(ZipCalendarListener listener) {
        if (this.zipCalendarProvider == null) {
            synchronized (this) {
                if (this.zipCalendarProvider == null) {
                    this.zipCalendarProvider =
                            new ZipCalendarProvider(this.context);
                }
            }
        }
        this.zipCalendarProvider.fetchCalendar(new ZipCalendarListener[]{listener});
    }
}
