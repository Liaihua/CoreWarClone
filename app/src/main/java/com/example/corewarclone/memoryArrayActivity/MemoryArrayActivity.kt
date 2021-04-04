package com.example.corewarclone.memoryArrayActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.corewarclone.R
import com.example.corewarclone.memoryArrayActivity.vm.Loader

// Я не знаю, как мне сделать эту активность: на Kotlin, или на C++?
// TODO Выясни, как пользоваться SurfaceView
class MemoryArrayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_array)

        val loader = Loader()
        loader.initializeMemoryArray(listOf("test.rbin"))
    }
}