package com.zip.zipandroid.utils

import android.text.InputFilter
import android.text.Spanned

class NoSpaceInputFilter:InputFilter {
    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        if (source.toString().contains(" ")) {
            return source.toString().replace(" ", "");
        }
        return null // 返回 null 表示不修改输入
    }

}