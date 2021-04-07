package com.example.corewarclone.memoryArrayActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.corewarclone.R
import com.example.corewarclone.memoryArrayActivity.vm.*

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

        val exec = Executor()
        // Проверка исполнителя на правильность работы. Данный код будет перенесен в Scheduler
        var cycles = 0
        while (cycles < CYCLES_UNTIL_TIE) {
            val warrior = Warriors.first()
            val offset = exec.execute(warrior, warrior.taskQueue.first, MemoryArray[warrior.taskQueue.first.instructionPointer])
            if(offset != null)
            {
                val IP = warrior.taskQueue.first.instructionPointer
                warrior.taskQueue.first.instructionPointer = calculateRound(MEMORY_ARRAY_SIZE, IP + offset)
            }
            else
            {
                warrior.taskQueue.remove(warrior.taskQueue.remove())
                break
            }
            cycles++
        }
    }

    fun stopExecution(view: View) {
        finish()
    }
}