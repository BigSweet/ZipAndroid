package com.zip.zipandroid.utils

import android.content.Context

object ZipLoadingUtils {
    var mDialog: ZipProgressDialog? = null

    fun show(context: Context) {
        if (mDialog != null && mDialog?.isShowing == true) return
        mDialog = ZipProgressDialog(context)
        mDialog!!.show()
    }

    fun dismiss() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
    }
}