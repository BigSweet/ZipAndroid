package com.zip.zipandroid.utils

import com.blankj.utilcode.util.ObjectUtils
import java.util.TreeMap

object SignUtils {
    var signRandomCode = "qwer1234"
    fun signParameter(paramterMap: TreeMap<String, Any?>, signKey: String?): String {
        val stringBuilder = StringBuilder()
        for ((_, value) in paramterMap) {
            if (!ObjectUtils.isEmpty(value)) {
                stringBuilder.append(value)
            }
        }

        val keyStr = stringBuilder.append(signKey)
        val md51 = ZipMd5Utils.md5(keyStr.toString())
        val md52 = ZipMd5Utils.md5(md51 + (signRandomCode))
        return md52
    }
}