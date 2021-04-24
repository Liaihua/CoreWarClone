package com.example.corewarclone.memoryArrayActivity.vm

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.SurfaceView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import com.example.corewarclone.memoryArrayActivity.WarriorsAdapter
import kotlinx.android.synthetic.main.activity_memory_array.view.*
import kotlin.math.roundToInt
import kotlin.random.Random

/*
* У нас есть массив с размером 80 : 100 ячеек
* Значит, мы можем отрисовать 79 и 99 линий по горизонтали и вертикали соответственно. Только как?
* Вдобавок, было бы удобнее хранить клетки в массиве, дабы не пытаться вручную манипулировать
* surfaceView. Как мне вообще заставить этот поток сознания обрести готовый вид?
* И еще: стоит ли сюда добавлять работу с warriors_recycler_view? Впридачу с убиранием выполненной инструкции
* */

class Visualizer(private val context: Context) {
    var surfaceView: SurfaceView =
        (context as Activity).findViewById(R.id.memory_array_surface_view)

    var warriorsRecyclerView: RecyclerView =
        (context as Activity).findViewById(R.id.warriors_recycler_view)

    // Массивы, используемые для рисования различимых ячеек программ
    val warriorsColors = arrayOf(Color.BLUE or Color.GREEN, Color.YELLOW or Color.RED)
    var warriorsToColors: HashMap<Int, Int> = hashMapOf()

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

        // Инициализация цветов, используемых для программ
        for ((counter, warrior) in Warriors.withIndex()) {
            warriorsToColors[warrior.id] = counter
        }

        // Присваивание цветов программам
        (context as Activity).runOnUiThread {
            for (item in 0 until warriorsRecyclerView.adapter!!.itemCount) {
                val warriorViewHolder =
                    (warriorsRecyclerView.findViewHolderForAdapterPosition(item) as WarriorsAdapter.ViewHolder)
                warriorViewHolder.setTextColor(warriorsColors[warriorsToColors[item]!!])
            }
        }
    }

    // Я думаю, его можно использовать для обновления клеток, соответствующих выполненным инструкциям
    fun drawMemoryArray(interrupted: Boolean) {
        if (!interrupted) {
            val paint = Paint()
            var rectIndices =
                Warriors.filter { !it.taskQueue.isEmpty() }.map {
                    Pair(it.id, it.taskQueue.first.instructionPointer)
                }
            for (rectIndex in rectIndices) {
                paint.color = warriorsColors[warriorsToColors[rectIndex.first]!!]

                val rect = rectangles[rectIndex.second]
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

                    // Рисование текущей инструкции
                    paint.color = Color.GRAY
                    val center = Pair(
                        rect.left + (rect.right - rect.left) / 2,
                        rect.top + (rect.bottom - rect.top) / 2
                    )
                    canvas.drawRect(
                        rect.left + (center.first - rect.left) / 2,
                        rect.top + (center.second - rect.top) / 2,
                        rect.right - (rect.right - center.first) / 2,
                        rect.bottom - (rect.bottom - center.second) / 2,
                        paint
                    )
                    surfaceView.holder.unlockCanvasAndPost(canvas)
                }

            }
        }
    }

    // Функция, которая перерисовывает предыдущую (выполненную) инструкцию
    fun drawPreviousInstructions(interrupted: Boolean) {
        if (!interrupted) {
            val paint = Paint()
            var rectIndices =
                Warriors.filter { !it.taskQueue.isEmpty() }.map {
                    Pair(it.id, it.taskQueue.first.previousInstructionPointer)
                }
            for (rectIndex in rectIndices) {
                if (rectIndex.second != -1) {
                    paint.color = warriorsColors[warriorsToColors[rectIndex.first]!!]

                    val rect = rectangles[rectIndex.second]
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

    // Рисование измененных инструкций (крестик на черном фоне)
    fun drawModifiedInstructions(modifiedInstructions: HashMap<Int, Int?>, interrupted: Boolean) {
        if (!interrupted) {
            val paint = Paint()
            val rectIndices =
                Warriors.filter { !it.taskQueue.isEmpty() }.map {
                    Pair(it.id, modifiedInstructions[it.id])
                }
            for (rectIndex in rectIndices) {
                if (rectIndex.second != null) {
                    paint.color = Color.BLACK
                    val rect = rectangles[rectIndex.second!!]
                    val canvas = surfaceView.holder?.lockCanvas(
                        Rect(
                            rect.left.roundToInt(),
                            rect.top.roundToInt(),
                            rect.right.roundToInt(),
                            rect.bottom.roundToInt()
                        )
                    )
                    if (canvas != null) {
                        canvas.drawRect(rect, paint)
                        paint.color = warriorsColors[warriorsToColors[rectIndex.first]!!]
                        canvas.drawLine(rect.left, rect.top, rect.right, rect.bottom, paint)
                        canvas.drawLine(rect.left, rect.bottom, rect.right, rect.top, paint)

                        surfaceView.holder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }
    }

    fun drawExitedInstruction(instructionPosition: Int, interrupted: Boolean) {
        if (!interrupted) {
            val paint = Paint()
            paint.color = Color.BLACK

            val rect = rectangles[instructionPosition]
            val canvas = surfaceView.holder?.lockCanvas(
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