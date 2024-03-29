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
import com.example.corewarclone.ProgramFileManager

const val ACTION_CHOOSE_DIR = 0xdead

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

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ACTION_CHOOSE_DIR && resultCode == RESULT_OK)
        {
            val dirString = programFileManager.getDirectoryPathFromUri(this, data!!.data!!)

            if (dirString != null) {
                programFileManager.saveCurrentDirectory(dirString)
                updateRecyclerView()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.choose_folder_menu_item -> {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, ACTION_CHOOSE_DIR)
            true
        }
        // Папочка тебя не забудет
        //R.id.settings_menu_item -> {
        //    true
        //}
        else -> super.onOptionsItemSelected(item)
    }

    private fun updateRecyclerView() {
        val pfRecyclerView = findViewById<RecyclerView>(R.id.programs_recycler_view)
        (pfRecyclerView.adapter as ProgramFileAdapter).ProgramFiles = programFileManager.listProgramFiles()
        pfRecyclerView.adapter?.notifyDataSetChanged()
    }

    // А для этого - ACTION_CREATE_DOCUMENT
    fun newFile(view: View) {
        if(programFileManager.currentDir == null)
            return
        val editorIntent = Intent(this, EditorActivity::class.java)
        startActivity(editorIntent)
    }
    // Задолбал уже отвлекаться
}
