package com.example.corewarclone.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import com.example.corewarclone.editorActivity.EditorActivity
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    private val programFileManager = ProgramFileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Так как MainActivity является стартовой точкой запуска приложения, думаю,
        // пока что такой способ инициализации свойства сойдет
        programFileManager.contextDir = programFileManager.contextDir ?: this.filesDir

        val programFileList = programFileManager.listProgramFiles()
        val programFileAdapter = ProgramFileAdapter(programFileList) {
            // Для данной лямбды потребуется создание Intent ACTION_OPEN_DOCUMENT
            val editorIntent = Intent(this, EditorActivity::class.java).apply {
                data = it.name.toUri()
            }
            startActivity(editorIntent)
        }
        val pfRecyclerView = findViewById<RecyclerView>(R.id.programs_recycler_view)

        pfRecyclerView.adapter = programFileAdapter
        pfRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //override fun onActivityResult() {

    //}

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    // Как насчет того, чтобы сохранять выбранную директорию в какой-нибудь файл приложения?
        R.id.choose_folder_menu_item -> {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            //startActivityForResult(intent, Intent.ACTION)
            true
        }
        R.id.settings_menu_item -> {
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    // А для этого - ACTION_CREATE_DOCUMENT
    fun newFile(view: View) {
        val editorIntent = Intent(this, EditorActivity::class.java)
        startActivity(editorIntent)
    }
    // Задолбал уже отвлекаться
}
