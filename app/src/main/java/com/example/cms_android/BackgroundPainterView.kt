package com.example.cms_android.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BackgroundPainterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Gradient (equivalent to Flutter's LinearGradient)
        val shader = LinearGradient(
            0f, 0f, width, height,
            intArrayOf(
                Color.parseColor("#FF000088"), // with alpha
                Color.parseColor("#FF337AEA")
            ),
            null,
            Shader.TileMode.CLAMP
        )

        paint.shader = shader
        paint.style = Paint.Style.FILL

        val path = Path().apply {
            moveTo(0f, 0f)                  // topLeft
            lineTo(width, 0f)               // topRight
            lineTo(width, height * 0.6f)    // bottomRight
            cubicTo(
                width * 0.98f, height * 0.62f,
                width * 0.94f, height * 0.61f,
                width * 0.04f, height * 0.42f
            )
            cubicTo(
                width * 0.02f, height * 0.417f,
                0f, height * 0.4f,
                0f, height * 0.4f
            )
            close()
        }

        canvas.drawPath(path, paint)
    }
}
