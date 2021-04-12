package com.example.corewarclone.memoryArrayActivity.vm

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceView
import com.example.corewarclone.R
import kotlinx.android.synthetic.main.activity_memory_array.view.*
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

    // Метод для начальной отрисовки сетки (ну или в данном случае - MemoryArray)
    fun initializeMemoryArrayImage() {
        val width = surfaceView.width / 80.0;
        val height = surfaceView.height / 100.0
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 1.0F

        // TODO Создать массив, в котором будут храниться данные о клетках
        // К тому же, придется добавить к этому массиву место для хранения id, которому принадлежит
        // данная клетка

        var canvas = surfaceView.holder.lockCanvas()
        var lineWidth = 0.0
        var lineHeight = 0.0
        repeat(80) {
            lineWidth += width
            canvas.drawLine(
                lineWidth.toFloat(),
                0.toFloat(),
                lineWidth.toFloat(),
                surfaceView.height.toFloat(),
                paint
            )
        }
        repeat(100) {
            lineHeight += height
            canvas.drawLine(
                0.toFloat(),
                lineHeight.toFloat(),
                surfaceView.width.toFloat(),
                lineHeight.toFloat(),
                paint
            )
        }
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

    // Я думаю, его можно использовать для обновления клеток, соответствующих инструкциям
    // Вопрос: а я могу создать еще один слой поверх основной канвы с "решеткой"?
    // Просто дело в том, что во время перерисовки очень заметно мерцание экрана
    fun drawMemoryArray(interrupted: Boolean) {
        if (!interrupted) {
            var canvas = surfaceView.holder.lockCanvas()
            var paint = Paint()

            paint.color = Color.BLUE
            canvas.drawText(
                rand.nextInt(16).toString(),
                surfaceView.width.toFloat() % rand.nextInt(),
                surfaceView.height.toFloat() % rand.nextInt(),
                paint
            )
            surfaceView.holder.unlockCanvasAndPost(canvas)
        }
    }
}