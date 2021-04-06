package com.example.corewarclone.memoryArrayActivity.vm

import java.util.*

// TODO Догадайся

const val CYCLES_UNTIL_TIE = 100000
const val MAX_TASKS = 64

var Warriors = arrayOf<Warrior>()

class Warrior {
    var id: Int = 0
    var name: String = ""
    var taskQueue: ArrayDeque<Task> = ArrayDeque()

}

class Task {
    var id: Int = 0
    var instructionPointer: Int = -1
}

class Scheduler {
    fun schedule() {}
}