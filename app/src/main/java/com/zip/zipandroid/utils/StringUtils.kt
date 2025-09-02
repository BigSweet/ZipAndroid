package com.zip.zipandroid.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

object StringUtils {
    fun getDate2String(time: Long, pattern: String?): String {
        val date = Date(time)
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }


    fun compareDate(date1: String?, date2: String?): Int {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val dt1 = df.parse(date1)
            val dt2 = df.parse(date2)
            return if (dt1.time > dt2.time) {
                1
            } else if (dt1.time < dt2.time) {
                -1
            } else {
                0
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return 0
    }

    fun compareDate1(date1: String?, date2: String?): Int {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            val dt1 = df.parse(date1)
            val dt2 = df.parse(date2)
            return if (dt1.time > dt2.time) {
                1
            } else if (dt1.time < dt2.time) {
                -1
            } else {
                0
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return 0
    }

    fun callPhone(context: Context, phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        context.startActivity(intent)
    }

    fun getVersionCode(mContext: Context): Int {
        var versionCode = 0
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    fun getVersionName(context: Context): String {
        var verName = ""
        try {
            verName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }

    fun hideNo(phoneNo: String): String {
        if (TextUtils.isEmpty(phoneNo)) {
            return phoneNo
        }
        val length = phoneNo.length
        val beforeLength = 0
        val afterLength = 4
        //替换字符串，当前使用“*”
        val replaceSymbol = "*"
        val sb = StringBuffer()
        for (i in 0 until length) {
            if (i < beforeLength || i >= length - afterLength) {
                sb.append(phoneNo[i])
            } else {
                sb.append(replaceSymbol)
            }
            val temp = i + 1
            if (temp < phoneNo.length && temp % 4 == 0) {
                sb.append(" ")
            }
        }
        return sb.toString()
    }

    fun getJson(fileName: String?, context: Context): String {
        //将json数据变成字符串
        val stringBuilder = StringBuilder()
        try {
            //获取assets资源管理器
            val assetManager = context.assets
            //通过管理器打开文件并读取
            val bf = BufferedReader(InputStreamReader(assetManager.open(fileName!!)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

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

    fun isNotBlank(arg: String?): Boolean {
        return isNotEmpty(arg)
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

    @JvmStatic
    fun isEquals(s1: String?, s2: String?): Boolean {
        if (null == s1 && null == s2) {
            return true
        }
        return if (null != s1 && null != s2) {
            if (s1 == s2) {
                true
            } else {
                false
            }
        } else false
    }

    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    @JvmStatic
    fun isNumeric(str: String?): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    fun isAmount(str: String?): Boolean {
        val pattern = Pattern.compile("^([1-9][0-9]*)+(.[0-9]{1,2})?\$")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * 纯字母
     *
     * @param data
     * @return
     */
    fun isChar(data: String): Boolean {
        var i = data.length
        while (--i >= 0) {
            val c = data[i]
            return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'
        }
        return true
    }


    /**
     * 将字符串加星号掩码
     *
     * @param prefixLen 前面几位不掩
     * @param suffixLen 后面几位不掩
     * @param str
     * @return
     */
    fun maskString(prefixLen: Int, suffixLen: Int, str: String): String {
        var suffixLen = suffixLen
        if (isEmpty(str)) {
            return str
        }
        try {
            if (prefixLen > str.length) {
                throw StringIndexOutOfBoundsException(prefixLen)
            }
            if (prefixLen + suffixLen > str.length) {
                suffixLen = str.length - prefixLen
            }
            val sb = StringBuilder()
            sb.append(str.substring(0, prefixLen))
            for (i in 0 until str.length - prefixLen - suffixLen) {
                sb.append("*")
            }
            sb.append(str.substring(str.length - suffixLen, str.length))
            return sb.toString()
        } catch (e: StringIndexOutOfBoundsException) {
            e.printStackTrace()
        }
        return str
    }

    fun isEmail(email: String?): Boolean {
        if (isEmpty(email)) {
            return false
        }
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        val p =
            Pattern.compile("^[A-Za-z0-9_\\-\\.\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z_-]+)+\$") //复杂匹配
        val m = p.matcher(email)
        return m.matches()
    }

}