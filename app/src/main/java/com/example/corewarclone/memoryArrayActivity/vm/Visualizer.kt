package com.example.corewarclone.memoryArrayActivity.vm

import android.app.Activity
import android.content.Context
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
        var canvas = surfaceView.holder.lockCanvas()
        canvas.drawRGB(20, 20, 20)
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

    // Я думаю, его можно использовать для обновления клеток, соответствующих инструкциям
    fun drawMemoryArray() {
        var canvas = surfaceView.holder.lockCanvas()
        var paint = Paint()
        paint.color = rand.nextInt()
        canvas.drawCircle(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), paint)
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}