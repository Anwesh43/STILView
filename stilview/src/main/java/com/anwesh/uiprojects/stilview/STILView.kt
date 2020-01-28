package com.anwesh.uiprojects.stilview

/**
 * Created by anweshmishra on 28/01/20.
 */

import android.content.Context
import android.app.Activity
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.MotionEvent

val lines : Int = 4
val scGap : Float = 0.02f / lines
val sizeFactor : Float = 3f
val strokeFactor : Int = 90
val delay : Long = 20
val foreColor : Int = Color.parseColor("#3F51B5")
val backColor : Int = Color.parseColor("#BDBDBD")
val triSizeFactor : Float = 5f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawArrow(size : Float, paint : Paint) {
    for (j in 0..1) {
        save()
        rotate(45f * (1 - 2 * j))
        drawLine(0f, 0f, size, 0f, paint)
        restore()
    }
}

fun Canvas.drawSquareLineArrow(i : Int, size : Float, scale : Float, paint : Paint) {
    val sf : Float = scale.sinify()
    val scDiv : Double = 1.0 / lines
    val j : Int = Math.floor(sf / scDiv).toInt()
    val sfi : Float = sf.divideScale(j, lines)
    val x : Float = 2 * size * sfi
    save()
    rotate(90f * i)
    save()
    translate(-size, -size)
    drawLine(0f, 0f, x, 0f, paint)
    if (i == j) {
        save()
        translate(x, 0f)
        drawArrow(size / triSizeFactor, paint)
        restore()
    }
    restore()
    restore()
}

fun Canvas.drawSquareLines(size : Float, scale : Float, paint : Paint) {
    for (j in 0..(lines - 1)) {
        drawSquareLineArrow(j, size, scale, paint)
    }
}

fun Canvas.drawSTIL(scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val size : Float = Math.min(w, h) / sizeFactor
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(w / 2, h / 2)
    drawSquareLines(size, scale, paint)
    restore()
}

class STILView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}
