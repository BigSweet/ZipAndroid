package com.zip.zipandroid.utils.phonedate.sms

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class ZipSMSMessage : Serializable {
    var body // content
            : String? = null
    var id // id in sms database
            : Long = 0
    var address //The address of the other party
            : String? = null
    var date: Long = 0
    var dateSent: Long = 0
    var type = 0
    var status = 0

    companion object {
        fun toJSONObject(message: ZipSMSMessage): JSONObject {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("body", message.body)
                jsonObject.put("id", message.id)
                jsonObject.put("address", message.address)
                jsonObject.put("date", message.date)
                jsonObject.put("dateSent", message.dateSent)
                jsonObject.put("type", message.type)
                jsonObject.put("status", message.status)
            } catch (ignored: JSONException) {
            }
            return jsonObject
        }

        fun jsonArrayFromArray(messages: Array<ZipSMSMessage>): JSONArray {
            val array = JSONArray()
            for (message in messages) {
                array.put(toJSONObject(message))
            }
            return array
        }
    }
}