package com.zip.zipandroid.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.bean.PhotoDataModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.NetworkInterface
import java.util.Collections
import java.util.UUID


object ProjectUtil {
    private const val TAG = "ProjectUtil"

    fun setStatusBarTranslucent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = activity.window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            activity.window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
    设置沉浸状态栏
     */
    fun setStatusBar(activity: Activity, titleView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val statusBarHeight = BarUtils.getStatusBarHeight()
            titleView.setPadding(0, statusBarHeight, 0, 0)
            titleView.layoutParams?.height = titleView.layoutParams?.height?.plus(statusBarHeight)
        }
    }


    /**
     * 获取时间差
     */
    fun getTimeDifference(startTime: Long, endTime: Long): Long {
        val diff = endTime - startTime
        var days = diff / (1000 * 60 * 60 * 24)
        val hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60)
        val second = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000
        return minutes
    }

    /**
     * 获取时间差
     */
    fun getTimeDifference(startTime: Long): Long {
        val diff = System.currentTimeMillis() - startTime
        var days = diff / (1000 * 60 * 60 * 24)
        return days
    }

    fun dpTopx(context: Context?, dipValue: Int): Int {
        var scale = context?.resources?.displayMetrics?.density!!
        return ((dipValue * scale + 0.5f).toInt())
    }

    fun getMxTime(time: String?): String {
        if (android.text.TextUtils.isEmpty(time)) {
            return ""
        }
        if (time?.contains("-")!!) {
            var month: String = ""
            val splitTime = time.split("-")
            when (splitTime[1]) {
                "1", "01" -> {
                    month = "ENE"
                }

                "2", "02" -> {
                    month = "FEB"
                }

                "3", "03" -> {
                    month = "MAR"
                }

                "4", "04" -> {
                    month = "ABR"
                }

                "5", "05" -> {
                    month = "MAY"
                }

                "6", "06" -> {
                    month = "JUN"
                }

                "7", "07" -> {
                    month = "JUL"
                }

                "8", "08" -> {
                    month = "AGO"
                }

                "9", "09" -> {
                    month = "SEP"
                }

                "10" -> {
                    month = "OCT"
                }

                "11" -> {
                    month = "NOV"
                }

                "12" -> {
                    month = "DIC"
                }
            }
            return splitTime[0] + " " + month + " " + splitTime[2]
        }
        return ""
    }

    fun getMacawTime(time: String?): String {
        if (android.text.TextUtils.isEmpty(time)) {
            return ""
        }
        if (time?.contains("-")!!) {
            var month: String = ""
            val splitTime = time.split("-")
            when (splitTime[0]) {
                "0" -> {
                    month = "ENE"
                }

                "1", "01" -> {
                    month = "FEB"
                }

                "2", "02" -> {
                    month = "MAR"
                }

                "3", "03" -> {
                    month = "ABR"
                }

                "4", "04" -> {
                    month = "MAY"
                }

                "5", "05" -> {
                    month = "JUN"
                }

                "6", "06" -> {
                    month = "JUL"
                }

                "7", "07" -> {
                    month = "AGO"
                }

                "8", "08" -> {
                    month = "SEP"
                }

                "9", "09" -> {
                    month = "OCT"
                }

                "10" -> {
                    month = "NOV"
                }

                "11" -> {
                    month = "DIC"
                }
            }
            return month + " " + splitTime[1]
        }
        return ""
    }

    fun getLoanTime(time: String?): String {
        if (android.text.TextUtils.isEmpty(time)) {
            return ""
        }
        if (time?.contains("-")!!) {
            var month: String = ""
            val splitTime = time.split("-")
            when (splitTime[0]) {
                "1", "01" -> {
                    month = "ENE"
                }

                "2", "02" -> {
                    month = "FEB"
                }

                "3", "03" -> {
                    month = "MAR"
                }

                "4", "04" -> {
                    month = "ABR"
                }

                "5", "5" -> {
                    month = "MAY"
                }

                "6", "06" -> {
                    month = "JUN"
                }

                "7", "07" -> {
                    month = "JUL"
                }

                "8", "08" -> {
                    month = "AGO"
                }

                "9", "09" -> {
                    month = "SEP"
                }

                "10", "10" -> {
                    month = "OCT"
                }

                "11" -> {
                    month = "NOV"
                }

                "12" -> {
                    month = "DIC"
                }
            }
            return month + " " + splitTime[1]
        }
        return ""
    }

    fun showInput(et: EditText, context: Context) {
        et.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    fun copyContentToClipboard(content: String?, context: Context) {
        //获取剪贴板管理器：
        val cm: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", content)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
    }

    private const val SD_PATH = "/sdcard/com.meti.co/pic/"
    private const val IN_PATH = "/finbee/pic/"

    /**
     * 随机生产文件名
     *
     * @return
     */
    fun generateFileName(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * 保存bitmap到本地
     * @param mBitmap
     * @return
     */
    fun saveBitmap(mBitmap: Bitmap): String? {
        val filePic: File
        val savePath: String = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            SD_PATH
        } else {
            (ZipApplication.instance!!.filesDir
                .absolutePath
                    + IN_PATH)
        }
        try {
            filePic = File(savePath + generateFileName() + ".jpg")
            if (!filePic.exists()) {
                filePic.parentFile.mkdirs()
                filePic.createNewFile()
            }
            val fos = FileOutputStream(filePic)
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return filePic.absolutePath
    }

    fun getPhotoLocation(context: Context?, type: String?) {
        var cursor: Cursor? = null
        var duration: String? = null
        val dateFormat = "dd-MM-yyyy"
        val datList = mutableListOf<PhotoDataModel>()
        if (type.equals("IMAGE")) {
            cursor = context?.contentResolver?.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        } else if (type.equals("VIDEO")) {
            cursor = context?.contentResolver?.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        }
        if (cursor == null) return
        try {
            while (cursor.moveToNext()) {
                var latitude: String? = null
                var longitude: String? = null
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                val displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                val mediaType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                val dateModified = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                val dateTaken = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                val height = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                val dataName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE))
                val width = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))

                if (type.equals("VIDEO")) {
                    duration = getVedioTotalTime(File(data))
                }

                var exif: ExifInterface? = null
                try {
                    exif = ExifInterface(path)
                    latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE).toString()
                    longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE).toString()
                    if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                        //转换经纬度格式
                        latitude = score2dimensionality(latitude).toString()
                        longitude = score2dimensionality(longitude).toString()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val model = PhotoDataModel(displayName, mediaType, title, dateTaken, dateTaken, dateModified, size, height, width, duration, latitude, longitude, data, dataName)
                datList.add(model)
            }
        } catch (exception: Exception) {
            ToastUtils.showShort(exception.toString())
        } finally {
            cursor.close()
        }
        if (type.equals("IMAGE")) {
            MMKV.defaultMMKV()?.putString("photoData", Gson().toJson(datList).toString())
        } else if (type.equals("VIDEO")) {
            MMKV.defaultMMKV()?.putString("videoData", Gson().toJson(datList).toString())
        }
    }

    private fun score2dimensionality(str: String?): Double {
        var dimensionality = 0.0
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            return dimensionality
        }

        //用 ，将数值分成3份
        val split = str?.split(",")
        for (i in split?.indices!!) {
            val s = split[i].split("/")
            //用112/1得到度分秒数值
            if (s.size <= 1 || TextUtils.isEmpty(s[0]) || TextUtils.isEmpty(s[1])) {
                break
            }
            val v = s[0].toDouble() / s[1].toDouble()
            //将分秒分别除以60和3600得到度，并将度分秒相加
            dimensionality = dimensionality + v / Math.pow(60.0, i.toDouble())
        }
        return dimensionality
    }

    fun getVedioTotalTime(vedioFile: File): String? {
        if (!vedioFile.exists()) {
//            L.e("视频文件不存在")
            return null
        }
        var timeString = ""
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(vedioFile.absolutePath)
            timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
        } catch (e: RuntimeException) {
        }

        return timeString
    }

    fun isDeviceInVPN(): Boolean {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (nif.name.equals("tun0") || nif.name.equals("ppp0")) {
                    Log.i("TAG_VPN", "isDeviceInVPN  current device is in VPN.")
                    return true
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun openGooglePlayForUrl(context: Context, url: String) {
        val currentPackageUri: Uri =
            Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, currentPackageUri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}