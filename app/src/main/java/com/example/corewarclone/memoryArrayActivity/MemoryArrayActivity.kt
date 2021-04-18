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
    private var binariesList : List<String>? = null
    private lateinit var loader: Loader
    private lateinit var schedulerThread: SchedulerThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_array)

        binariesList = intent.getBundleExtra("BINARIES")
                             .getStringArray("BINARIES")?.toList()
        if (binariesList != null) {
            // TODO Сделать инициализацию изображения игрового поля перед запуском игры
            // Хотя, может и не стоит?
            schedulerThread = SchedulerThread(scheduler, context = this)
        }
        else
            finish()
    }

    fun startExecution(view: View) {
        if (!schedulerThread.isAlive) {
            if(binariesList != null) {
                loader = Loader()
                MemoryArray = loader.initializeMemoryArray(binariesList!!)
                // TODO Сделать отображение диалогового окна без необходимости в runOnUiThread
                schedulerThread.start()
            }
            else
                finish()
        }
    }

    fun stopExecution(view: View) {
        schedulerThread.interrupt()
        finish()
    }

    override fun onStop() {
        super.onStop()
        schedulerThread.interrupt()
    }

    // Эксперименты с SurfaceView
    /*
    * Я подумал вот о чем: я могу заставить работать кнопку с шагом до тех пор,
    * пока я не вызову startExecution.
    * */
    fun stepExecution(view: View) {
        if (schedulerThread.isAlive)
            return
        val surfaceView = findViewById<SurfaceView>(R.id.memory_array_surface_view)
        val canvas = surfaceView.holder.lockCanvas()
        val rand = Random
        canvas.drawRGB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255))
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}