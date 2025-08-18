package com.zip.zipandroid.base

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat


fun Context.dp2px(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
        .toInt()
}


fun Context.getFoColor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}


class Context {

}