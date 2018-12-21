package com.zhang.common.commonutils

/**
 * Created by zhang .
 * DATA: 2018/8/10 .
 * Description : String_Util
 */
object StringUtil {

    /**
     * 判空
     */
    fun isEmpty(s: String?): Boolean {
        return s == null || s.isEmpty()
    }

    /**
     * Return `""` if string equals null.
     */
    fun null2Length0(s: String?): String {
        return s ?: ""
    }

    /**
     * 首字母大写
     */
    fun upperFirstLetter(s: String?): String {
        if (s == null || s.isEmpty()) return ""
        if (!Character.isLowerCase(s[0])) return s
        return (s[0] - 32) + s.substring(1)
    }

    /**
     * 首字母小写
     */
    fun lowerFirstLetter(s: String?): String {
        if (s == null || s.isEmpty()) return ""
        return if (!Character.isUpperCase(s[0])) s else (s[0].toInt() + 32).toChar().toString() + s.substring(1)
    }

    /**
     * 反转字符串
     */
    fun reverse(s: String?): String {
        if (s == null) return ""
        val len = s.length
        if (len <= 1) return s
        var mid = len.shr(1)
        var chars = s.toCharArray()
        var c: Char
        for (i in 0 until mid) {
            c = chars[i]
            chars[i] = chars[len - i - 1]
            chars[len - i - 1] = c
        }
        return String(chars)
    }
}