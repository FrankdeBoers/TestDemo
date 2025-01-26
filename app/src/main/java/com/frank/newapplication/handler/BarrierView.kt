package com.frank.newapplication.handler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.random.Random

class BarrierView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    View(context, attrs, defStyle) {
    var i = 1f
    val paint = Paint()

    fun start() {
        Thread {
            Log.i("FrankTest", "Barrier## start")
            while (true) {
                invalidate()
                Thread.sleep(Random.nextLong(400))
            }
        }.start()

        Thread {
            Log.i("FrankTest", "Barrier## start")
            while (true) {
                invalidate()
                Thread.sleep(Random.nextLong(400))
            }
        }.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i("FrankTest", "Barrier## onMeasure")
        setMeasuredDimension(
            getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
            getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        )
        Thread.sleep(Random.nextLong(400))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.i("FrankTest", "Barrier## onDraw")
        i += 100f
        if (i > 2000f) {
            i = 0f
        }
        paint.isAntiAlias = false
        paint.color = Color.BLUE
        paint.strokeWidth = 3f
        canvas.drawCircle(i, i, 90f, paint)
    }

}