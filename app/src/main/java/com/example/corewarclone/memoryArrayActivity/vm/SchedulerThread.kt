package com.example.corewarclone.memoryArrayActivity.vm

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.corewarclone.R

// TODO Решить вопрос с отображением диалогового окна (возможно, с использованием доп. потока и join'ом)
class SchedulerThread(var loadedScheduler: Scheduler? = null, var context: Context) : Thread() {
    private var scheduler : Scheduler = loadedScheduler ?: Scheduler()
    override fun interrupt() {
        super.interrupt()
        scheduler.interrupted = true
    }
    override fun run() {
        if(scheduler.visualizer == null)
            scheduler.newVisualizerFromContext(context)
        val result = scheduler.schedule()

        if(scheduler.interrupted)
            return

        val alertDialog = AlertDialog.Builder(context)

        if(result == null) {
            alertDialog.setMessage(R.string.tie_string)
        }
        else {
            alertDialog.setMessage("${R.string.winner_string} ${result.name}")
        }
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener {_,_ ->
            run {
                (context as Activity).finish()
            }
        })
        alertDialog.show()
    }
}