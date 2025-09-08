package com.zip.zipandroid.utils.phonedate.calllog

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class ZipCallLog : Serializable {
    var date: Long = 0
    var duration = 0
    var number // number of other party
            : String? = null
    var name //cached name
            : String? = null
    var type = 0
    fun toJSONObject(): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("date", date)
            jsonObject.put("duration", duration)
            jsonObject.put("number", number)
            jsonObject.put("name", name)
            jsonObject.put("type", type)
        } catch (e: JSONException) {
        }
        return jsonObject
    }

    companion object {
        fun jsonArrayFromArray(zipCallLogs: Array<ZipCallLog>): JSONArray {
            val jsonArray = JSONArray()
            for (callLog in zipCallLogs) {
                jsonArray.put(callLog.toJSONObject())
            }
            return jsonArray
        }
    }
}