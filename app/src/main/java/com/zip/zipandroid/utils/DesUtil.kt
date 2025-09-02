package com.zip.zipandroid.utils

import android.text.TextUtils
import android.util.Base64
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

object DesUtil {
    private const val HEX = "0123456789ABCDEF"
    private const val TRANSFORMATION =
        "DES/CBC/PKCS5Padding" //DES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private const val IVPARAMETERSPEC = "01020304" ////初始化向量参数，AES 为16bytes. DES 为8bytes.
    private const val ALGORITHM = "DES" //DES是加密方式
    private const val SHA1PRNG = "SHA1PRNG" //// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法

    /*
     * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
     */
    fun generateKey(): String? {
        try {
            val localSecureRandom =
                SecureRandom.getInstance(SHA1PRNG)
            val bytes_key = ByteArray(20)
            localSecureRandom.nextBytes(bytes_key)
            return toHex(bytes_key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    //二进制转字符
    fun toHex(buf: ByteArray?): String {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(HEX[(b.toInt() shr 4) and 0x0f]).append(HEX[b.toInt() and 0x0f])
    }

    // 对密钥进行处理
    @Throws(Exception::class)
    private fun getRawKey(key: String): Key {
        val dks = DESKeySpec(key.toByteArray())
        val keyFactory =
            SecretKeyFactory.getInstance(ALGORITHM)
        return keyFactory.generateSecret(dks)
    }

    fun encode(key: String, data: String): String? {
        return encode(key, data.toByteArray())
    }

    fun encode(key: String, data: ByteArray?): String? {
        return try {
            val cipher =
                Cipher.getInstance(TRANSFORMATION)
            val iv =
                IvParameterSpec(IVPARAMETERSPEC.toByteArray())
            cipher.init(
                Cipher.ENCRYPT_MODE,
                getRawKey(key),
                iv
            )
            val bytes = cipher.doFinal(data)
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    fun decode(key: String, data: String?): String? {
        return decode(
            key,
            Base64.decode(data, Base64.DEFAULT)
        )
    }

    fun decode(key: String, data: ByteArray?): String? {
        return try {
            val cipher =
                Cipher.getInstance(TRANSFORMATION)
            val iv =
                IvParameterSpec(IVPARAMETERSPEC.toByteArray())
            cipher.init(
                Cipher.DECRYPT_MODE,
                getRawKey(key),
                iv
            )
            val original = cipher.doFinal(data)
            String(original)
        } catch (e: Exception) {
            null
        }
    }

    fun Base64Encode(content: String): String {
        return if (!TextUtils.isEmpty(content)) {
            Base64.encodeToString(
                content.toByteArray(),
                Base64.DEFAULT
            )
        } else ""
    }

    fun Base64Decode(base64Data: String?): String {
        return if (!TextUtils.isEmpty(base64Data)) {
            String(Base64.decode(base64Data, Base64.DEFAULT))
        } else ""
    }

    fun MD5Encode(content: String): String {
        if (!TextUtils.isEmpty(content)) {
            var md5: MessageDigest? = null
            try {
                md5 = MessageDigest.getInstance("MD5")
                val bytes = md5.digest(content.toByteArray())
                val result = StringBuilder()
                for (b in bytes) {
                    var temp = Integer.toHexString(b.toInt() and 0xff)
                    if (temp.length == 1) {
                        temp = "0$temp"
                    }
                    result.append(temp)
                }
                return result.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return ""
    }
}