package com.zip.zipandroid.ktx

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

/** view 扩展 */
var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }


var View.visiblein
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

var View.invisible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.GONE
    }

fun View.hide(gone: Boolean = true) {
    visibility = if (gone) View.GONE else View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun ViewGroup.inflateLayout(@LayoutRes layoutResId: Int, parent: ViewGroup? = this, attachToRoot: Boolean = false) =
    LayoutInflater.from(context).inflate(
        layoutResId, parent, attachToRoot)

fun View.takeScreenShot(): Bitmap {
    this.isDrawingCacheEnabled = true
    this.buildDrawingCache(true)

    // creates immutable clone
    val b = Bitmap.createBitmap(this.drawingCache)
    this.isDrawingCacheEnabled = false // clear drawing cache
    return b
}

/**
 * Take screen shot of just the View without any constraints
 */
fun View.takeScreenShotOfJustView(): Bitmap {
    this.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
    this.layout(0, 0, rootView.measuredWidth, rootView.measuredHeight)
    return takeScreenShot()
}

fun TextureView.takeScreenShot(): Bitmap? {
    return this.bitmap
}

fun View.takeScreenShotOfRootView(): Bitmap {
    return rootView.takeScreenShot()
}

fun View.setOnDelayClickListener(delay: Long = 1500, listener: View.OnClickListener) {
    setOnClickListener(OnDelayClickListener(delay, listener1 = listener))
}

//fun View.setOnDelayClickListener(delay: Long = 1500, listener: (view: View) -> Unit) {
//    setOnClickListener(OnDelayClickListener(delay, listener2 = listener))
//}
fun View.setOnDelayClickListener(
    delayMillis: Long = 600,
    onClick: (View) -> Unit,
) {
    val scope = MainScope()
    var isClickable = true

    setOnClickListener { view ->
        if (isClickable) {
            isClickable = false
            onClick(view)
            scope.launch(Dispatchers.Main) {
                delay(delayMillis)
                isClickable = true
            }
        }
    }
}


private class OnDelayClickListener(val delay: Long, val listener1: View.OnClickListener? = null, val listener2: ((view: View) -> Unit)? = null) : View.OnClickListener {
    private var clickTime: Long = 0
    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (abs(currentTime - clickTime) > delay) {
            clickTime = currentTime
            listener1?.onClick(v)
            listener2?.invoke(v)
        }
    }
}