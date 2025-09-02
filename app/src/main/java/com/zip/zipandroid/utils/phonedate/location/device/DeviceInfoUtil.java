package com.zip.zipandroid.utils.phonedate.location.device;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.Context.WIFI_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.zip.zipandroid.ZipApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;


public class DeviceInfoUtil {

    private Context context;
    private static int MB = 1024 * 1024;

    public DeviceInfoUtil(Context context) {
        this.context = context;
    }


    public String getAppName() {
        PackageManager packageManager = context.getPackageManager();
        try {
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getAppVersionName() {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getAppVersionCode() {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns the unique device ID, for example, the IMEI for GSM and the MEID
     * or ESN for CDMA phones. Return null if device ID is not available.
     *
     * @return
     */
    public String getIdForVendor() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(TELEPHONY_SERVICE);
            String deviceId = "";
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                deviceId = telephonyManager.getDeviceId();
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (deviceId.isEmpty()) {
                    deviceId = telephonyManager.getImei();
                }
            }

            if (TextUtils.isEmpty(deviceId)) {
                deviceId = "NA";
            }
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NA";
    }

    public String getIdForAdvertising() {
//        AdvertisingIdClient.Info idInfo = null;
//        try {
//            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String advertId = null;
//        try{
//            advertId = idInfo.getId();
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
//
//        return advertId;
        return "NA";
    }

    public String getDisplayResolution() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels + "x" + metrics.heightPixels;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getTotalMemory() {
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            am.getMemoryInfo(memInfo);
            return memInfo.totalMem / MB + " MB";
        } else {
            return 0 + " MB";
        }
    }

    public String getTotalStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize / MB + " MB";
        } else {
            return stat.getTotalBytes() / MB + " MB";
        }
    }

    public int getCPUCount() {
        return getValueFromKeyValueFile("/proc/cpuinfo", "processor", true).length;
    }

    private final static String CurPath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";

    public String getCPUSpeed() {
        int result = 0;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(CurPath);
            br = new BufferedReader(fr);
            String text = br.readLine();
            result = Integer.parseInt(text.trim());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return String.valueOf(result);
    }

    public String getDeviceArch() {
        String arch = System.getProperty("os.arch");
        if (TextUtils.isEmpty(arch)) {
            arch = "NA";
        }
        return arch;
    }


    public String getIPAddress() {

        WifiManager wm = (WifiManager) this.context.getSystemService(WIFI_SERVICE);
        if (wm != null && wm.getConnectionInfo() != null) {
            int ipAddress = wm.getConnectionInfo().getIpAddress();
            return String.format(Locale.US, "%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        }
        return "NA";
    }

    public String getSystemStartupTime() {
        long time = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        long runTime = System.currentTimeMillis() - time;
        return String.valueOf(runTime);
    }

    public String getRoot() {
        if (isSuEnable1() || isSuEnable2()) {
            return "true";
        }
        return "false";
    }

    public String getDeveloperOption() {
        String open = "";
        boolean enableAdb = false;
        try {
            enableAdb = (Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0);
        } catch (Exception e) {
            enableAdb = false;
        }

        if (enableAdb) {
            open = "1";
            Log.i("手机开发者选项------", "打开了");
        } else {
            open = "0";
            Log.i("手机开发者选项------", "没打开");
        }

        return open;
    }

    public String getScreenBrightness() {
        if (null != context) {
            try {
                int screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                Log.i("手机屏幕亮度------", screenBrightness + "");
                return String.valueOf(screenBrightness);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getElectricity() {
        int level = 0;
        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        level = batteryInfoIntent.getIntExtra("level", 0);
        int batterySum = batteryInfoIntent.getIntExtra("scale", 100);
        int percentBattery = 100 * level / batterySum;
        Log.i("手机当前电量------", percentBattery + "");
        return String.valueOf(percentBattery);
    }

    public String getImei() {
        String deviceId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        }
        Log.e("imei ==== ", deviceId);
        return deviceId;
    }

    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public String getWiFiName() {
        WifiManager wm = (WifiManager) ZipApplication.Companion.getInstance().getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wm != null) {
            WifiInfo winfo = wm.getConnectionInfo();
            if (winfo != null) {
                String s = winfo.getSSID();
                if (s.length() > 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                    return s.substring(1, s.length() - 1);
                }
            }
        }
        return "";
    }


    /**
     * 是否存在su命令，并且有执行权限
     *
     * @return 存在su命令，并且有执行权限返回true
     */
    public static boolean isSuEnable1() {
        File file = null;
        String[] paths = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/", "/su/bin/"};
        try {
            for (String path : paths) {
                file = new File(path + "su");
                if (file.exists() && file.canExecute()) {
                    return true;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return false;
    }

    /**
     * 是否存在busybox命令，并且有执行权限
     *
     * @return 存在busybox命令，并且有执行权限返回true
     */
    public static boolean isSuEnable2() {
        File file = null;
        String[] paths = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/", "/su/bin/"};
        try {
            for (String path : paths) {
                file = new File(path + "busybox");
                if (file.exists() && file.canExecute()) {
                    return true;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return false;
    }

    public static boolean checkDeviceDebuggable() {
        String buildTags = Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }


    public boolean isNetworkConnected() {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public String getMacAddress() {
        String mac = "02:00:00:00:00:00";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacFromFile();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        if (TextUtils.isEmpty(mac)) {
            mac = "02:00:00:00:00:00";
        }
        return mac;
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @param context
     * @return
     */
    private String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     *
     * @return
     */
    private String getMacFromFile() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }

    /**
     * Android 7.0 之后
     * <p>
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return
     */
    private String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    public String getNetworkType() {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        String type = null;
        if (networkInfo != null && networkInfo.isAvailable()) {
            type = networkInfo.getTypeName();
        }
        if (TextUtils.isEmpty(type)) {
            type = "NA";
        }
        return type;
    }

    public String getCarrierName() {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String name = null;
        if (manager != null) {
            name = manager.getNetworkOperatorName();
        }
        if (TextUtils.isEmpty(name)) {
            name = "NA";
        }
        return name;
    }

    public String getCountry() {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String country = null;
        if (manager != null) {
            country = manager.getNetworkCountryIso();
        }
        if (TextUtils.isEmpty(country)) {
            country = "NA";
        }
        return country;
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }


    public Boolean isSimulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public String getAndroidId() {
        return Settings.System.getString(this.context.getContentResolver(), Settings.System.ANDROID_ID);
    }

    public String getSimId() {
//        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED) {
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (tm != null) {
//                String simSerialNumber = tm.getSimSerialNumber();
//                if (!TextUtils.isEmpty(simSerialNumber)) {
//                    return simSerialNumber;
//                }
//            }
//        }
        return "NA";
    }

    public String getUUID() {
        SharedPreferences mShare = context.getSharedPreferences("DFPK_sysCacheMap", Context.MODE_PRIVATE);
        String uuid = null;
        if (mShare != null) {
            SharedPreferences.Editor editor = mShare.edit();
            if (editor != null) {
                uuid = mShare.getString("uuid", "");
                if (TextUtils.isEmpty(uuid)) {
                    uuid = UUID.randomUUID().toString();
                    editor.putString("uuid", uuid).apply();
                }
            }
        }
        return uuid;
    }

    private String[] getValueFromKeyValueFile(String file, String key, Boolean isFindAll) {

        Closeable[] closableList = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            LineNumberReader lineNumberReader = new LineNumberReader(inputStreamReader);
            closableList = new Closeable[]{fileInputStream, inputStreamReader, lineNumberReader};

            ArrayList<String> valueList = new ArrayList<String>();
            String line;
            while ((line = lineNumberReader.readLine()) != null) {
                if (line.contains(key)) {
                    String strValue = line.substring(line.indexOf(":") + 1, line.length());
                    if (isFindAll) {
                        valueList.add(strValue.trim());
                        continue;
                    }
                    return new String[]{strValue.trim()};
                }
            }
            String[] retValues = new String[(valueList.size())];
            return valueList.toArray(retValues);
        } catch (Exception ex) {

        } finally {
            if (closableList != null) {
                for (int i = 0; i < closableList.length; i++) {
                    if (closableList[i] != null) {
                        try {
                            closableList[i].close();
                        } catch (IOException e) {
                        }
                    }
                }
            }

        }
        return new String[0];
    }

    public String getGenaralDeviceId() {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            if (isSimulator()) {
                deviceId.append("v");
            }

            //Device Id ,maybe IMEI
            String did = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                try {
                    did = getIdForVendor();
                } catch (Exception e) {
                    e.printStackTrace();
                    did = "NA";
                }
            }

            if (!TextUtils.isEmpty(did) && !"NA".equals(did) && charCount(did, '0') != did.length() && charCount(did, '*') != did.length()) {
                deviceId.append("did");
                deviceId.append("_");
                deviceId.append(did);
                return deviceId.toString();
            }
            //wifi mac地址
            String mac = getMacAddress();
            if (!TextUtils.isEmpty(mac) && mac.length() == 17 && !mac.equals("02:00:00:00:00:00")) {
                deviceId.append("mac");
                deviceId.append("_");
                deviceId.append(mac);
                return deviceId.toString();
            }

            //android id
            String androidId = getAndroidId();
            if (!TextUtils.isEmpty(androidId)) {
                deviceId.append("aid");
                deviceId.append("_");
                deviceId.append(androidId);
                return deviceId.toString();
            }

            //sim序列号（simSerialNumber）
            String sim = getSimId();
            if (!TextUtils.isEmpty(sim) && !"NA".equals(sim)) {
                deviceId.append("sim");
                deviceId.append("_");
                deviceId.append(sim);
                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID();
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("uuid");
                deviceId.append("_");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("e").append("uuid").append("_").append(getUUID());
        }
        return deviceId.toString();

    }

    public String getTimeZoneDisplayNameShort() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    public String getEmployMemory() {
        String employMemory = 0 + " MB";
        try {
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                am.getMemoryInfo(memInfo);
                employMemory = (memInfo.totalMem - memInfo.availMem) / (1024 * 1024) + " MB";
            }
        } catch (Exception e) {

        }
        return employMemory;
    }

    public String getEmployStorage() {
        String employStorage = 0 + " MB";
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                employStorage = (stat.getBlockCount() - stat.getAvailableBlocks()) * stat.getBlockSize() / (1024 * 1024) + " MB";
            } else {
                employStorage = (stat.getTotalBytes() - stat.getAvailableBytes()) / (1024 * 1024) + " MB";
            }
        } catch (Exception e) {

        }
        return employStorage;
    }

    public String getIsCharging() {
        String isCharging = "NA";
        try {
            Intent batteryBroadcast = context.registerReceiver(null,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            boolean charging = batteryBroadcast.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) != 0;
            if (charging) {
                isCharging = "true";
            } else {
                isCharging = "false";
            }
        } catch (Exception e) {

        }
        return isCharging;
    }

    public String getSystemBattery() {
        String percentBattery = "NA";
        try {
            Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryInfoIntent.getIntExtra("level", 0);
            int batterySum = batteryInfoIntent.getIntExtra("scale", 100);
            percentBattery = 100 * level / batterySum + "%";
        } catch (Exception e) {

        }
        return percentBattery;
    }

    public String getSystemRunTime() {
        long runTime = SystemClock.elapsedRealtime();
        int h = (int) (runTime / 1000 / 60 / 60);
        int m = (int) ((runTime - (h * 60 * 60 * 1000)) / 60 / 1000);
        int s = (int) (runTime - (m * 60 * 1000) - (h * 60 * 60 * 1000)) / 1000;
        String bootTime = h + " h " + m + " m " + s + " s";
        return bootTime;
    }

    private int charCount(String src, char c) {
        int count = 0;
        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public static boolean IsExistCard(Context context) {
        boolean result = false;
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
//            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method getState = storageVolumeClazz.getMethod("getState");
            Object obj = null;
            try {
                obj = getVolumeList.invoke(mStorageManager);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            final int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(obj, i);
//                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                String state = (String) getState.invoke(storageVolumeElement);
                if (removable && state.equals(Environment.MEDIA_MOUNTED)) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idForVendor", this.getIdForVendor());
            jsonObject.put("idForAdvertising", this.getIdForAdvertising());
            jsonObject.put("displayResolution", this.getDisplayResolution());
            jsonObject.put("model", this.getModel());
            jsonObject.put("totalMemory", this.getTotalMemory());
            jsonObject.put("totalStorage", this.getTotalStorage());
            jsonObject.put("cpuCount", this.getCPUCount());
            jsonObject.put("cpuSpeed", this.getCPUSpeed());
            jsonObject.put("deviceArch", this.getDeviceArch());
            jsonObject.put("ipAddress", this.getIPAddress());
            jsonObject.put("macAddress", this.getMacAddress());
            jsonObject.put("carrierName", this.getCarrierName());
            jsonObject.put("networkType", this.getNetworkType());
            jsonObject.put("country", this.getCountry());
            jsonObject.put("osVersion", this.getOSVersion());
            jsonObject.put("isSimulator", this.isSimulator());
            jsonObject.put("androidId", this.getAndroidId());
            jsonObject.put("simSerialNumber", this.getSimId());
            jsonObject.put("uuid", this.getUUID());
            jsonObject.put("timeZoneDisplayNameShort", this.getTimeZoneDisplayNameShort());
            jsonObject.put("generalDeviceId", this.getGenaralDeviceId());
            jsonObject.put("employMemory", this.getEmployMemory());
            jsonObject.put("employStorage", this.getEmployStorage());
            jsonObject.put("isCharging", this.getIsCharging());
            jsonObject.put("systemBattery", this.getSystemBattery());
            jsonObject.put("systemRunTime", this.getSystemRunTime());
        } catch (JSONException e) {

        }
        return jsonObject;
    }

    public String toJSONString() {
        return this.toJSONObject().toString();
    }
}
