package com.zip.zipandroid.bean

class ZipCallLogBean {
    var sunan: String? = null
    var lambar: String? = null
    var kwananWata: String? = null
    var tsawonLokaci: String? = null

    //            this.type = getCallTypeEnumString(type);
    var nauIn: String? = null

    private fun getCallTypeEnumString(type: Int): String {
        var desc = "UNKNOWN_TYPE"
        when (type) {
            1 -> desc = "INCOMING_TYPE"
            2 -> desc = "OUTGOING_TYPE"
            3 -> desc = "MISSED_TYPE"
            4 -> desc = "VOICEMAIL_TYPE"
            5 -> desc = "REJECTED_TYPE"
            6 -> desc = "BLOCKED_TYPE"
            7 -> desc = "ANSWERED_EXTERNALLY_TYPE"
        }
        return desc
    }
}