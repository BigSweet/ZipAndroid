package com.zip.zipandroid.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class VerticalDashedLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = 0xFFABBFF5.toInt() // 默认灰色
        style = Paint.Style.STROKE
        strokeWidth = 2f.dpToPx(context) // 虚线粗细
        pathEffect = DashPathEffect(floatArrayOf(5f.dpToPx(context), 5f.dpToPx(context)), 0f) // 虚线样式：5dp实线，5dp间隔
        isAntiAlias = true
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        path.reset()
        path.moveTo(centerX, 0f)
        path.lineTo(centerX, height.toFloat())

        canvas.drawPath(path, paint)
    }

    // 设置虚线颜色
    fun setDashColor(color: Int) {
        paint.color = color
        invalidate()
    }

    // 设置虚线样式
    fun setDashStyle(strokeWidth: Float, dashLength: Float, gapLength: Float) {
        paint.strokeWidth = strokeWidth.dpToPx(context)
        paint.pathEffect = DashPathEffect(floatArrayOf(dashLength.dpToPx(context), gapLength.dpToPx(context)), 0f)
        invalidate()
    }
}

// 扩展函数：dp转px
fun Float.dpToPx(context: Context): Float {
    return this * context.resources.displayMetrics.density
}

