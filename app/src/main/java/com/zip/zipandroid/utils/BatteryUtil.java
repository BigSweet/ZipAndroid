package com.zip.zipandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import java.io.File;


public class BatteryUtil {
    /**
     * 获取电池容量
     */
    public static String getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(batteryCapacity);
    }

    public static String getSystemBattery(Context context) {
        int level = 0;
        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        level = batteryInfoIntent.getIntExtra("level", 0);
        int batterySum = batteryInfoIntent.getIntExtra("scale", 100);
        int percentBattery= 100 *  level / batterySum;
        return String.valueOf(percentBattery);
    }

    /**
     * 主动获取当前电池是否在充电 , 即数据线是否插在手机上
     * @return
     */
    public static boolean isBatteryCharging(Context context){
        boolean isBatteryCharging = false;
        // 主动发送包含是否正在充电状态的广播 , 该广播会持续发送
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // 注册广播接受者
        Intent intent = context.registerReceiver(null, intentFilter);

        // 获取充电状态
        int batteryChargeState = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        // 判定是否是 AC 交流电充电
        boolean isAc = batteryChargeState == BatteryManager.BATTERY_PLUGGED_AC;
        // 判断是否是 USB 充电
        boolean isUsb = batteryChargeState == BatteryManager.BATTERY_PLUGGED_USB;
        // 判断是否是 无线充电
        boolean isWireless = batteryChargeState == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        // 如何上述任意一种为 true , 说明当前正在充电
        isBatteryCharging = isAc || isUsb || isWireless;

        return isBatteryCharging;
    }

    /**
     * 主动获取当前电池是否在充电 , 即数据线是否插在手机上
     * @return
     */
    public static boolean isBatteryCharg(Context context){
        boolean isBatteryCharging = false;
        // 主动发送包含是否正在充电状态的广播 , 该广播会持续发送
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // 注册广播接受者
        Intent intent = context.registerReceiver(null, intentFilter);

        // 获取充电状态
        int batteryChargeState = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        // 判定是否是 AC 交流电充电
        boolean isAc = batteryChargeState == BatteryManager.BATTERY_PLUGGED_AC;

        // 如何上述任意一种为 true , 说明当前正在充电
        isBatteryCharging = isAc;

        return isBatteryCharging;
    }

    /**
     * 主动获取当前电池是否在充电 , 即数据线是否插在手机上
     * @return
     */
    public static boolean isBatteryUsbCharg(Context context){
        boolean isBatteryCharging = false;
        // 主动发送包含是否正在充电状态的广播 , 该广播会持续发送
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // 注册广播接受者
        Intent intent = context.registerReceiver(null, intentFilter);

        // 获取充电状态
        int batteryChargeState = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        // 判断是否是 USB 充电
        boolean isUsb = batteryChargeState == BatteryManager.BATTERY_PLUGGED_USB;

        // 如何上述任意一种为 true , 说明当前正在充电
        isBatteryCharging = isUsb ;

        return isBatteryCharging;
    }

    public static boolean isOpenDebug(Context context){
        boolean enableAdb = (Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);
        return enableAdb;
    }

    public static String getTotalRom() {
        File dataDir = Environment.getDataDirectory();
        StatFs stat = new StatFs(dataDir.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long size = totalBlocks * blockSize;
        long GB = 1024 * 1024 * 1024;
        final long[] deviceRomMemoryMap = {2*GB, 4*GB, 8*GB, 16*GB, 32*GB, 64*GB, 128*GB, 256*GB, 512*GB, 1024*GB, 2048*GB};
        String[] displayRomSize = {"2GB","4GB","8GB","16GB","32GB","64GB","128GB","256GB","512GB","1024GB","2048GB"};
        int i;
        for(i = 0 ; i < deviceRomMemoryMap.length; i++) {
            if(size <= deviceRomMemoryMap[i]) {
                break;
            }
            if(i == deviceRomMemoryMap.length) {
                i--;
            }
        }
        return displayRomSize[i];
    }
}
