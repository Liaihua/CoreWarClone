package com.example.corewarclone.memoryArrayActivity.vm

import android.app.AlertDialog
import android.content.Context
import com.example.corewarclone.R

class SchedulerThread(var loadedScheduler: Scheduler? = null, var context: Context) : Thread() {
    private var scheduler : Scheduler = loadedScheduler ?: Scheduler()
    override fun run() {
        val result = scheduler.schedule()
        val alertDialog = AlertDialog.Builder(context)
        if(result == null) {
            alertDialog.setMessage(R.string.tie_string)
        }
        else {
            alertDialog.setMessage("${R.string.winner_string} ${result.name}")
        }
        alertDialog.show()
    }
}