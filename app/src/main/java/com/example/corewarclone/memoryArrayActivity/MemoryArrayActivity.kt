package com.example.corewarclone.memoryArrayActivity

import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import com.example.corewarclone.R
import com.example.corewarclone.memoryArrayActivity.vm.*
import kotlin.random.Random

// TODO Выясни, как пользоваться SurfaceView
class MemoryArrayActivity : AppCompatActivity() {
    private var scheduler = Scheduler()
    private lateinit var loader : Loader
    private lateinit var schedulerThread : SchedulerThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_array)

        // Проверка загрузчика на правильность работы
        loader = Loader()
        // TODO Переделать MemoryArrayActivity для запуска множества программ (в моем случае - двух, но кто знает?)
        MemoryArray = loader.initializeMemoryArray(listOf("imp.rbin", "test.rbin"))


        schedulerThread = SchedulerThread(context = this)
    }

    // TODO Сделать обновление компонентов (таких как MemoryArray, Scheduler и пр.) после нажатия кнопки
    fun startExecution(view: View) {
        if(!schedulerThread.isAlive) {
            loader = Loader()
            MemoryArray = loader.initializeMemoryArray(listOf("imp.rbin", "test.rbin"))
            // TODO Сделать отображение диалогового окна без необходимости в runOnUiThread
            schedulerThread.start()
        }
    }

    fun stopExecution(view: View) {
        finish()
    }

    // Эксперименты с SurfaceView
    /*
    * Я подумал вот о чем: я могу заставить работать кнопку с шагом до тех пор,
    * пока я не вызову startExecution.
    * */
    fun stepExecution(view: View) {
        if(schedulerThread.isAlive)
            return
        val surfaceView = findViewById<SurfaceView>(R.id.memory_array_surface_view)
        val canvas = surfaceView.holder.lockCanvas()
        val rand = Random
        canvas.drawRGB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255))
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}