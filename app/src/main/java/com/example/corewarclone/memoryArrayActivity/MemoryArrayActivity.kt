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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_array)

        // Проверка загрузчика на правильность работы
        val loader = Loader()
        MemoryArray = loader.initializeMemoryArray(listOf("imp.rbin"))

    }

    fun startExecution(view: View) {
        val schedulerThread = SchedulerThread()
        schedulerThread.start()
    }

    fun stopExecution(view: View) {
        finish()
    }

    // Эксперименты с SurfaceView
    fun stepExecution(view: View) {
        val surfaceView = findViewById<SurfaceView>(R.id.memory_array_surface_view)
        val canvas = surfaceView.holder.lockCanvas()
        val rand = Random
        canvas.drawRGB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255))
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}