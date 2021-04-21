package com.example.corewarclone.memoryArrayActivity.vm

import android.app.AlertDialog
import android.content.Context
import java.util.*

const val CYCLES_UNTIL_TIE = 100000
const val MAX_TASKS = 64

var Warriors = ArrayDeque<Warrior>()

class Warrior {
    var id: Int = 0
    var name: String = ""
    var taskQueue: ArrayDeque<Task> = ArrayDeque()
}

class Task {
    var id: Int = 0
    // Вопрос: а так ли нужен этот previousInstructionPointer?
    var previousInstructionPointer : Int = -1
    var instructionPointer: Int = -1
}

// Кажется, у меня появилась одна маленькая идея, как можно реализовать запуск и шаг на конкретную инструкцию.
// В чем она заключается: у нас есть глобальная переменная (нда, гениально), которую мы постоянно
// проверяем в цикле перед тем, как выполнить инструкцию. Когда мы нажимаем на кнопку "Шаг", переменная
// устанавливается в значение false, тем самым прерывая выполнение. В блоке else может быть код, который
// сохраняет контекст MemoryArray перед остановкой. Затем каждый раз, когда мы вызываем метод, привязанный
// к кнопке "Шаг", мы извлекаем контекст, выполняем 1 инструкцию и сохраняем контекст. Когда мы нажимаем на кнопку
// "Запуск" снова, мы устанавливаем глобальную переменную в значение true, извлекаем контекст, и продолжаем
// выполнять код

// Такое своеобразное подобие корутин.

class Scheduler {
    var interrupted: Boolean = false

    var visualizer: Visualizer? = null
    private var cycles = 0
    private val exec = Executor()
    var modifiedInstructions = hashMapOf<Int, Int?>()

    // Функция для цикличной перестановки элементов в деке
    private fun shiftRound(deque: ArrayDeque<Task>) {
        if (deque.count() > 1) {
            val shiftedItem = deque.pollFirst()
            deque.add(shiftedItem)
        }
    }

    fun getCycles() = cycles

    fun newVisualizerFromContext(context: Context) {
        visualizer = Visualizer(context)
        visualizer!!.initializeMemoryArrayImage()
    }

    // Я вот думаю, стоит ли мне добавлять сюда код для обновления графической части?
    fun schedule(): Warrior? {
        // Так как мы уже инициализировали список программ и задач, мы будем просто объявлять здесь цикл с выполнением задач
        // Результатом метода schedule является вывод той программы, которая смогла "остаться в живых". Или же null в случае ничьи

        while (cycles < CYCLES_UNTIL_TIE && !interrupted) {
            visualizer!!.drawMemoryArray(interrupted)
            if (Warriors.count() == 1)
                return Warriors.first()
            stepCycle()
                // TODO Посмотреть, что будет, если переставить строку снизу вверх
            visualizer!!.drawPreviousInstructions(interrupted)
            visualizer!!.drawModifiedInstructions(modifiedInstructions, interrupted)

            cycles++
        }

        cycles = 0

        return if (Warriors.count() > 1 || Warriors.count() == 0)
            null
        else
            Warriors.first()
    }

    fun stepCycle() {
        modifiedInstructions = hashMapOf()
        for (warrior in Warriors) {
            if (warrior.taskQueue.isEmpty()) {
                Warriors.remove(warrior)
                continue
            }

            val task = warrior.taskQueue.first
            val offset = exec.execute(warrior, task, MemoryArray[task.instructionPointer])
            if (offset != null) {
                val iP = task.instructionPointer
                task.previousInstructionPointer = iP
                task.instructionPointer = calculateRound(MEMORY_ARRAY_SIZE, iP + offset)
                modifiedInstructions[warrior.id] = exec.modifiedInstruction
            } else {
                // Стоит ли сюда добавлять код по замене прямоугольника на черный?
                warrior.taskQueue.remove(task)
            }
            shiftRound(warrior.taskQueue)
        }
    }

    // Функция, используемая для исполнения инструкций по нажатию кнопки
    fun stepExecution() : Warrior? {
        if(cycles < CYCLES_UNTIL_TIE) {
            visualizer!!.drawMemoryArray(interrupted)
            if (Warriors.count() == 1) {
                // Нужно найти способ подменить строчку внизу
                return Warriors.first()
            }

            stepCycle()
            visualizer!!.drawPreviousInstructions(interrupted)
            visualizer!!.drawModifiedInstructions(modifiedInstructions, interrupted)

            cycles++
            return null
        }
        else return null
    }
}

