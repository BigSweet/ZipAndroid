package com.zip.zipandroid.utils.phonedate.applist

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class ZipInstalledApp : Serializable {
    var firstInstallTime: String? = null
    var lastUpdateTime: String? = null
    var packageName: String? = null
    var versionCode = 0
    var versionName: String? = null
    var appLabel: String? = null

    companion object {
        private fun toJSONObject(info: ZipInstalledApp): JSONObject {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("firstInstallTime", info.firstInstallTime)
                jsonObject.put("lastUpdateTime", info.lastUpdateTime)
                jsonObject.put("packageName", info.packageName)
                jsonObject.put("versionCode", info.versionCode)
                jsonObject.put("versionName", info.versionName)
                jsonObject.put("appLabel", info.appLabel)
            } catch (ignored: JSONException) {
            }
            return jsonObject
        }

        fun jsonArrayFromArray(apps: Array<ZipInstalledApp>): JSONArray {
            val array = JSONArray()
            for (app in apps) {
                array.put(toJSONObject(app))
            }
            return array
        }
    }
}