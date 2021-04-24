package com.example.corewarclone.memoryArrayActivity.vm

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.corewarclone.R

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

        showDialog(result)
    }

    fun showDialog(result: Warrior?) {
        (context as Activity).runOnUiThread {
            val alertDialog = AlertDialog.Builder(context)

            if (result == null) {
                alertDialog.setMessage(R.string.tie_string)
            } else {
                alertDialog.setMessage("${context.getString(R.string.winner_string)} ${result.name}")
            }
            alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                run {
                    (context as Activity).finish()
                }
            })
            alertDialog.setOnCancelListener {
                run { (context as Activity).finish() }
            }
            alertDialog.show()
        }
    }
}