package com.zip.zipandroid.utils

object ZipStringUtils {

    /**
     * 增加0
     *
     * @param num
     * @return
     */
    fun addZero(num: Int): String {
        return (if (num < 10) "0" else "") + num
    }
}