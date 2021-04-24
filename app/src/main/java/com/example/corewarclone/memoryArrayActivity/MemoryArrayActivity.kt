package com.example.corewarclone.memoryArrayActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import com.example.corewarclone.memoryArrayActivity.vm.*

class MemoryArrayActivity : AppCompatActivity() {
    private var scheduler = Scheduler()
    private var binariesList : List<String>? = null
    private var loader: Loader? = null
    private lateinit var schedulerThread: SchedulerThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_array)

        binariesList = intent.getBundleExtra("BINARIES")
                             .getStringArray("BINARIES")?.toList()
        if (binariesList != null) {
            if(loader == null) {
                loader = Loader()
                MemoryArray = loader!!.initializeMemoryArray(binariesList!!)
            }
            schedulerThread = SchedulerThread(scheduler, context = this)

            val warriorsAdapter = WarriorsAdapter()

            val warriorsRecyclerView = findViewById<RecyclerView>(R.id.warriors_recycler_view)
            warriorsRecyclerView.adapter = warriorsAdapter
            warriorsRecyclerView.layoutManager = LinearLayoutManager(this)
        }
        else
            finish()
    }

    fun startExecution(view: View) {
        if (!schedulerThread.isAlive) {
            if(binariesList != null) {
                val warriorsRecyclerView = findViewById<RecyclerView>(R.id.warriors_recycler_view)
                for (item in 0 until warriorsRecyclerView.adapter!!.itemCount) {
                    val warriorViewHolder =
                        (warriorsRecyclerView.findViewHolderForAdapterPosition(item) as WarriorsAdapter.ViewHolder)
                    warriorViewHolder.removeDebugTextViews()
                }

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
        if(!schedulerThread.isAlive) {

            if (scheduler.visualizer == null)
                scheduler.newVisualizerFromContext(this)

            val result = scheduler.stepExecution()
            findViewById<RecyclerView>(R.id.warriors_recycler_view).adapter?.notifyDataSetChanged()
            if(result != null) {
                schedulerThread.showDialog(result)
            }
        }
    }
}