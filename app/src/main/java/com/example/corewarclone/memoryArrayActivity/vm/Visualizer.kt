package com.example.corewarclone.memoryArrayActivity.vm

import android.content.Context
import android.view.SurfaceView

/*
* TODO Сделать изображение массива
* У нас есть массив с размером 80 : 100 ячеек
* Значит, мы можем отрисовать 79 и 99 линий по горизонтали и вертикали соответственно. Только как?
* Вдобавок, было бы удобнее хранить клетки в массиве, дабы не пытаться вручную манипулировать
* surfaceView. Как мне вообще заставить этот поток сознания обрести готовый вид?
* */

class Visualizer(context: Context) {
    var surfaceView: SurfaceView = SurfaceView(context)

    // Метод для начальной отрисовки сетки (ну или в данном случае - MemoryArray)
    fun initializeMemoryArrayImage() {
        var canvas = surfaceView.holder.lockCanvas()
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

    // Я думаю, его можно использовать для обновления клеток, соответствующих инструкциям
    fun drawMemoryArray() {
        var canvas = surfaceView.holder.lockCanvas()
        surfaceView.holder.unlockCanvasAndPost(canvas)

    }
}