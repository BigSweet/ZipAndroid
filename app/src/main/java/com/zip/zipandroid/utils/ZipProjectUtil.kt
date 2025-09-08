package com.zip.zipandroid.utils

import android.content.Context
import android.database.Cursor
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.bean.PhotoDataModel
import java.io.File
import java.io.IOException


object ZipProjectUtil {


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


}