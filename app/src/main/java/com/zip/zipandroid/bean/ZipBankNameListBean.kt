package com.zip.zipandroid.bean

import com.zip.zipandroid.utils.Cn2Spell
import java.util.Locale

class ZipBankNameListBean : ArrayList<ZipBankNameListBeanItem>()

data class ZipBankNameListBeanItem(
    val bankName: String,
    val icon: String,
    val id: Int,
    val payType: Int,
) {

    var pinyin: String = "" // 姓名对应的拼音

    var firstLetter: String = "" // 拼音的首字母

    fun getNamePin() {
        pinyin = Cn2Spell.getPinYin(bankName) // 根据姓名获取拼音
        if (pinyin == null || pinyin.length == 0) {
            firstLetter = "#"
        } else {
            firstLetter = pinyin.substring(0, 1).uppercase(Locale.getDefault()) // 获取拼音首字母并转成大写
            if (!firstLetter.matches("[A-Z]".toRegex())) { // 如果不在A-Z中则默认为“#”
                firstLetter = "#"
            }
        }
    }

}