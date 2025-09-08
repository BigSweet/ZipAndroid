package com.zip.zipandroid.utils

import java.util.regex.Pattern

object StringUtils {


    /**
     * 判断一个字符串不是空的（服务可能会传null作为字符串内空，将null认为是空的）
     *
     * @param arg
     * @return
     */
    @JvmStatic
    fun isNotEmpty(arg: String?): Boolean {
        if (null == arg) {
            return false
        }
        if ("" == arg || "" == arg.trim { it <= ' ' }) {
            return false
        }
        if ("null" == arg || "null" == arg.trim { it <= ' ' }) {
            return false
        }
        return if ("" == replaceBlank(arg)) {
            false
        } else true
    }



    @JvmStatic
    fun isEmpty(str: String?): Boolean {
        if (null == str || "" == str) {
            return true
        } else if ("" == str.trim { it <= ' ' }) {
            return true
        } else if ("" == replaceBlank(str)) {
            return true
        } else if ("null" == str) {
            return true
        } else if ("null" == str.trim { it <= ' ' }) {
            return true
        }
        return false
    }

    fun replaceBlank(str: String?): String {
        var dest = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\\\\r|\\\\n")
            val m = p.matcher(str)
            dest = m.replaceAll("")
        }
        return dest
    }



}