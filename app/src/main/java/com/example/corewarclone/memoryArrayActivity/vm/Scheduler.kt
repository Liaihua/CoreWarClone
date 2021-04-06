package com.example.corewarclone.memoryArrayActivity.vm

import java.util.*

// TODO Догадайся

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