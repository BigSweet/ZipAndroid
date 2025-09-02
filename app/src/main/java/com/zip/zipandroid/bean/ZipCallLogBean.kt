package com.zip.zipandroid.bean

class ZipCallLogBean {
    var name: String? = null
    var number: String? = null
    var date: String? = null
    var duration: String? = null

    //            this.type = getCallTypeEnumString(type);
    var type: String? = null

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