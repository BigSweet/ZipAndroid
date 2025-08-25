package com.zip.zipandroid.utils

object EmailValidator {
    private const val LOCAL_PART_MAX_LENGTH = 64
    private const val DOMAIN_PART_MAX_LENGTH = 255
    private const val LABEL_MAX_LENGTH = 63

    // 允许的特殊字符 (不包括点号)
    private val ALLOWED_SPECIAL_CHARS = setOf(
        '!', '#', '$', '%', '&', '\'', '*', '+', '-', '/', '=',
        '?', '^', '_', '`', '{', '|', '}', '~'
    )

    fun isValid(email: String): Boolean {
        // 转换为小写
        val normalizedEmail = email.lowercase()

        // 1. 基本结构检查
        if (normalizedEmail.count { it == '@' } != 1) return false

        val parts = normalizedEmail.split("@")
        if (parts.size != 2) return false

        val localPart = parts[0]
        val domainPart = parts[1]

        // 2. 本地部分验证
        if (!isValidLocalPart(localPart)) return false

        // 3. 域名部分验证
        if (!isValidDomainPart(domainPart)) return false

        return true
    }

    private fun isValidLocalPart(localPart: String): Boolean {
        // 长度检查
        if (localPart.isEmpty() || localPart.length > LOCAL_PART_MAX_LENGTH) {
            return false
        }

        // 开头和结尾不能是特殊字符或点号
        val firstChar = localPart[0]
        val lastChar = localPart[localPart.length - 1]
        if (ALLOWED_SPECIAL_CHARS.contains(firstChar) ||
            ALLOWED_SPECIAL_CHARS.contains(lastChar) ||
            firstChar == '.' || lastChar == '.') {
            return false
        }

        // 连续特殊字符或连续点检查
        var prevChar: Char? = null
        for (char in localPart) {
            when {
                char.isLetterOrDigit() -> {} // 允许
                char == '.' -> {
                    if (prevChar == '.') return false // 不能连续两个点
                }
                ALLOWED_SPECIAL_CHARS.contains(char) -> {
                    if (prevChar in ALLOWED_SPECIAL_CHARS) return false // 不能连续特殊字符
                }
                else -> return false // 非法字符
            }
            prevChar = char
        }

        return true
    }

    private fun isValidDomainPart(domainPart: String): Boolean {
        // 整体长度检查
        if (domainPart.isEmpty() || domainPart.length > DOMAIN_PART_MAX_LENGTH) {
            return false
        }

        // 分割标签
        val labels = domainPart.split(".")
        if (labels.size < 2) return false // 至少有一个点和两个部分

        for (label in labels) {
            // 标签长度检查
            if (label.isEmpty() || label.length > LABEL_MAX_LENGTH) {
                return false
            }

            // 开头和结尾不能是短横线
            if (label.startsWith('-') || label.endsWith('-')) {
                return false
            }

            // 字符检查
            for (char in label) {
                if (!char.isLetterOrDigit() && char != '-') {
                    return false
                }
            }
        }

        return true
    }
}
