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
* TODO Сделать изображение массива
* У нас есть массив с размером 80 : 100 ячеек
* Значит, мы можем отрисовать 79 и 99 линий по горизонтали и вертикали соответственно. Только как?
* Вдобавок, было бы удобнее хранить клетки в массиве, дабы не пытаться вручную манипулировать
* surfaceView. Как мне вообще заставить этот поток сознания обрести готовый вид?
* */

class Visualizer(context: Context) {
    var surfaceView: SurfaceView =
        (context as Activity).findViewById(R.id.memory_array_surface_view)
    val rand = Random

    var rectangles = arrayOf<RectF>()

    // Метод для начальной отрисовки сетки (ну или в данном случае - MemoryArray)
    fun initializeMemoryArrayImage() {
        val width = surfaceView.width.toFloat() / 80.0F
        val height = surfaceView.height.toFloat() / 64.0F
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 1F

        // TODO Создать массив, в котором будут храниться данные о клетках
        // К тому же, придется добавить к этому массиву место для хранения id, которому принадлежит
        // данная клетка

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
        lineWidth = 0.0F
        lineHeight = 0.0F
        for(currentWidth in 0..79)
        {


            for(currentHeight in 0..63)
            {
                // TODO Переделать создание массива (чтобы все было в строчку, а не по диагонали)
                var rectF = RectF(
                lineWidth + 0.5F,
                lineHeight + 0.5F,
                lineWidth + width - 0.5F,
                lineHeight + height - 0.5F)
                rectangles += rectF
                lineWidth += width
                lineHeight += height
            }
        }
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

    // Я думаю, его можно использовать для обновления клеток, соответствующих инструкциям
    // Вопрос: а я могу создать еще один слой поверх основной канвы с "решеткой"?
    // Просто дело в том, что во время перерисовки очень заметно мерцание экрана
    fun drawMemoryArray(interrupted: Boolean) {
        if (!interrupted) {
            // lockCanvas также может брать параметр Rect в качестве области рисования
            // Почему бы мне этим не воспользоваться?
            val paint = Paint()
            paint.color = Color.BLUE
            for (rect in rectangles) {
                var canvas = surfaceView.holder?.lockCanvas(Rect(rect.left.roundToInt(), rect.top.roundToInt(), rect.right.roundToInt(), rect.bottom.roundToInt()))
                if(canvas != null) {
                    canvas.drawRect(rect, paint)
                    surfaceView.holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}