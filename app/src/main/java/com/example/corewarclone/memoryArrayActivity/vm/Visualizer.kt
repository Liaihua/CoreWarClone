package com.example.corewarclone.memoryArrayActivity.vm

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.SurfaceView
import com.example.corewarclone.R
import kotlinx.android.synthetic.main.activity_memory_array.view.*
import kotlin.math.roundToInt
import kotlin.random.Random

/*
* У нас есть массив с размером 80 : 100 ячеек
* Значит, мы можем отрисовать 79 и 99 линий по горизонтали и вертикали соответственно. Только как?
* Вдобавок, было бы удобнее хранить клетки в массиве, дабы не пытаться вручную манипулировать
* surfaceView. Как мне вообще заставить этот поток сознания обрести готовый вид?
* */

class Visualizer(context: Context) {
    var surfaceView: SurfaceView =
        (context as Activity).findViewById(R.id.memory_array_surface_view)
    val rand = Random

    // Наш массив с клетками - не единственная вещь, которая нас интересует
    // Обдумай, что мы будем рисовать:
    // 1. Инструкции программ
    // 2. Указатели на инструкцию (для текущей задачи, или для всех сразу?)
    var rectangles = arrayOf<RectF>()

    // Метод для начальной отрисовки сетки (ну или в данном случае - MemoryArray)
    fun initializeMemoryArrayImage() {
        val width = surfaceView.width.toFloat() / 80.0F
        val height = surfaceView.height.toFloat() / 64.0F
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 1F

        var canvas = surfaceView.holder.lockCanvas()
        var lineWidth = 0.0F
        var lineHeight = 0.0F
        repeat(79) {
            lineWidth += width
            canvas.drawLine(
                lineWidth,
                0.toFloat(),
                lineWidth,
                surfaceView.height.toFloat(),
                paint
            )
        }
        repeat(63) {
            lineHeight += height
            canvas.drawLine(
                0.toFloat(),
                lineHeight,
                surfaceView.width.toFloat(),
                lineHeight,
                paint
            )
        }

        // Создание массива прямоугольников
        lineWidth = 0.0F
        lineHeight = 0.0F

        for (currentWidth in 0..63) {
            for (currentHeight in 0..79) {
                var rectF = RectF(
                    // 0.5F - Место для отрисованной сетки
                    lineWidth + 0.5F,
                    lineHeight + 0.5F,
                    lineWidth + width - 0.5F,
                    lineHeight + height - 0.5F
                )
                rectangles += rectF
                lineWidth += width
            }
            lineWidth = 0.0F
            lineHeight += height
        }
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

    // TODO Сделать наконец отрисовку массива
    // Я думаю, его можно использовать для обновления клеток, соответствующих инструкциям
    fun drawMemoryArray(interrupted: Boolean) {
        // Проверка работы с массивом прямоугольников
        // Ну и еще эксперименты с рисованием массива, не у нас здесь есть одна проблема:
        // перед рисованием у нас идет фильтрация массива. Мы делаем ее КАЖДЫЙ РАЗ,
        // ничего не записывая в промежуточный буфер. Плюс, мы КАЖДЫЙ РАЗ перерисовываем старые ячейки.
        // Из-за этого рисование постепенно замедляется
        if (!interrupted) {
            val paint = Paint()
            paint.color = Color.WHITE
            var rectIndices =
                Warriors.filter { !it.taskQueue.isEmpty() }.map {
                    it.taskQueue.first.instructionPointer
                }
            for (rectIndex in rectIndices) {

                    val rect = rectangles[rectIndex]
                    var canvas = surfaceView.holder?.lockCanvas(
                        Rect(
                            rect.left.roundToInt(),
                            rect.top.roundToInt(),
                            rect.right.roundToInt(),
                            rect.bottom.roundToInt()
                        )
                    )
                    if (canvas != null) {
                        canvas.drawRect(rect, paint)
                        surfaceView.holder.unlockCanvasAndPost(canvas)
                    }

            }
        }
    }
}