package com.zip.zipandroid.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.view.activityManager
import com.zip.zipandroid.view.telephonyManager
import com.zip.zipandroid.view.wifiManager
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.TimeZone

/**
 * Function:上帝视角页面
 * Time:2022/11/30
 * Author:huage
 */
object SystemUtil {
    private const val FILENAME_PROC_MEMINFO = "/proc/meminfo"
    val deviceBrand: String
        get() = Build.BRAND
    val deviceName: String
        get() = Build.DEVICE

    // 单位为KB
    val totalMem: Long
        get() {
            try {
                val fr = FileReader(FILENAME_PROC_MEMINFO)
                val br = BufferedReader(fr)
                val text = br.readLine()
                val array = text.split("\\s+").toTypedArray()
                // 单位为KB
                return java.lang.Long.valueOf(array[1])
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return -1
        }

    fun getFreeMem(): Double {
        val utilAvailMem: Double = getMemoryInfo(ZipApplication.instance!!.applicationContext)!!.availMem * 1.0 / (1024 * 1024)
        return utilAvailMem
    }

    fun getMainStorage(): String{
        
        if(ZipApplication.instance!!.applicationContext.filesDir==null){
            return ""
        }
        return ZipApplication.instance!!.applicationContext.filesDir.path
    }

    fun getExternalStorage(): String{
        if(ZipApplication.instance!!.applicationContext.getExternalFilesDir("")==null){
            return ""
        }
        return ZipApplication.instance!!.applicationContext.getExternalFilesDir("")?.path ?: ""
    }

    fun getMemoryCardsize(): Long {
        /*获取存储卡路径*/
        val sdcardDir: File = Environment.getExternalStorageDirectory()
        /*StatFs 看文件系统空间使用情况*/
        val statFs = StatFs(sdcardDir.getPath())
        val blockSize: Long = statFs.getBlockSizeLong()
        val totalSize: Long = statFs.getBlockCountLong()
        return blockSize * totalSize
    }

    /**
     * 获得sd卡剩余容量，即可以大小
     * @return
     */
    fun getSdAvaliableSize(): Long {
        val path = Environment.getExternalStorageDirectory()
        val statFs = StatFs(path.path)
        val blockSize = statFs.blockSizeLong
        val availableBlocks = statFs.availableBlocksLong
        return blockSize * availableBlocks
    }

    fun getSimOperator(): String?{
        return ZipApplication.instance!!.applicationContext.telephonyManager?.simOperatorName
    }

    fun getSimOperatorName(): String?{
        return ZipApplication.instance!!.applicationContext.telephonyManager?.simOperator
    }

    fun getPhoneType(): String{
        return ZipApplication.instance!!.applicationContext.telephonyManager?.phoneType.toString()
    }

    fun getMCC(): String{
        val networkOperator =
            ZipApplication.instance!!.applicationContext.telephonyManager?.networkOperator
        if(networkOperator==null){
            return ""
        }
        if(networkOperator.length==0){
            return ""
        }
        return networkOperator.substring(0, 3)
    }

    fun getMNC(): String{
        val networkOperator =
            ZipApplication.instance!!.applicationContext.telephonyManager?.networkOperator
        if(networkOperator==null){
            return ""
        }
        if(networkOperator.length==0){
            return ""
        }
        return networkOperator.substring(3)
    }

    /**
     * 获取当前时区
     * @return
     */
    fun  getCurrentTimeZone():String {
        var tz = TimeZone.getDefault()
        var strTz = tz.getDisplayName(false, TimeZone.SHORT)
        return strTz;
    }

    @SuppressLint("HardwareIds")
    fun getIMSI() :String{
        var tm = ZipApplication.instance!!.applicationContext.telephonyManager
        if (tm == null) return ""
        if(tm.subscriberId==null||tm.subscriberId==""){
            return ""
        }
        return tm.subscriberId
    }

    fun getDbm() :String{
        val wifiManager = ZipApplication.instance!!.applicationContext.wifiManager
        val connectionInfo = wifiManager?.connectionInfo
        if (wifiManager==null||connectionInfo == null) return ""
        return connectionInfo.rssi.toString()
    }

    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    fun isHaveSDCard():String{
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return "1";
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            return "0";
        }
        else{
            return "0";
        }
    }

   fun getAppTotalMemory():Float{
       var am = ZipApplication.instance!!.applicationContext.activityManager
       if (am == null) return 0f
       val totalMemory = (Runtime.getRuntime().totalMemory() * 1.0 / (1024 * 1024)).toFloat()
       return totalMemory
   }

    fun getAppMaxmemory():Float{
        var am = ZipApplication.instance!!.applicationContext.activityManager
        if (am == null) return 0f
        val totalMemory = (Runtime.getRuntime().maxMemory() * 1.0 / (1024 * 1024)).toFloat()
        return totalMemory
    }

    fun getAppFreememory():Float{
        var am = ZipApplication.instance!!.applicationContext.activityManager
        if (am == null) return 0f
        val totalMemory = (Runtime.getRuntime().freeMemory() * 1.0 / (1024 * 1024)).toFloat()
        return totalMemory
    }

    /**
     * Get memory info of device.
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    fun getMemoryInfo(context: Context): ActivityManager.MemoryInfo? {
        val am = context.activityManager
        val mi = ActivityManager.MemoryInfo()
        am?.getMemoryInfo(mi)
        return mi
    }

    fun getramTotalSize():Double{
        val utilTotalMem: Double = getMemoryInfo(ZipApplication.instance!!.applicationContext)!!.totalMem * 1.0 / (1024 * 1024)
        return utilTotalMem
    }
}