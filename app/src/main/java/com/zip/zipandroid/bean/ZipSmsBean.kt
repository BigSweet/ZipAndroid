package com.zip.zipandroid.bean

class ZipSmsBean {
    var address: String? = null
    var body: String? = null
    var date: String? = null
    var dateSent: String? = null
    var status: String? = null
        private set
    var type: String? = null
        private set

    fun setStatus(status: Int) {
        this.status = getStatusEnumString(status)
    }

    fun setType(type: Int) {
        this.type = getTypeEnumString(type)
    }

    private fun getStatusEnumString(status: Int): String {
        var desc = "STATUS_UNKNOWN"
        when (status) {
            -1 -> desc = "STATUS_NONE"
            0 -> desc = "STATUS_COMPLETE"
            32 -> desc = "STATUS_PENDING"
            64 -> desc = "STATUS_FAILED"
        }
        return desc
    }

    private fun getTypeEnumString(type: Int): String {
        var desc = "MESSAGE_TYPE_UNKNOWN"
        when (type) {
            0 -> desc = "MESSAGE_TYPE_ALL"
            1 -> desc = "MESSAGE_TYPE_INBOX"
            2 -> desc = "MESSAGE_TYPE_SENT"
            3 -> desc = "MESSAGE_TYPE_DRAFT"
            4 -> desc = "MESSAGE_TYPE_OUTBOX"
            5 -> desc = "MESSAGE_TYPE_FAILED"
            6 -> desc = "MESSAGE_TYPE_QUEUED"
        }
        return desc
    }
}