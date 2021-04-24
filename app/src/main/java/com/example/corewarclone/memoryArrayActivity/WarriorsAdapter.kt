package com.example.corewarclone.memoryArrayActivity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import com.example.corewarclone.memoryArrayActivity.vm.MemoryArray
import com.example.corewarclone.memoryArrayActivity.vm.Warrior
import com.example.corewarclone.memoryArrayActivity.vm.Warriors

class WarriorsAdapter : RecyclerView.Adapter<WarriorsAdapter.ViewHolder>(){
    var warriors = Warriors ?: null
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var warrior: Warrior? = null
        val warriorNameTextView = view.findViewById<TextView>(R.id.warrior_name_text_view)
        var warriorTaskTextView = view.findViewById<TextView>(R.id.warrior_task_id_text_view)
        var warriorInstructionTextView = view.findViewById<TextView>(R.id.warrior_instruction_text_view)
        var warriorInstructionPointerTextView = view.findViewById<TextView>(R.id.warrior_instruction_pointer_text_view)

        val warriorTaskLabel = view.findViewById<TextView>(R.id.warrior_task_label)
        val warriorInstructionPointerLabel = view.findViewById<TextView>(R.id.warrior_ip_label)

        fun bind(warrior: Warrior) {
            if (!warrior.taskQueue.isEmpty()) {
                this.warrior = warrior
                warriorNameTextView.text = warrior.name
                warriorTaskTextView.text = warrior.taskQueue.first.id.toString()
                var instruction = MemoryArray[warrior.taskQueue.first.instructionPointer]
                // TODO Заменить на более удобное для чтения представление (скорее всего, понадобится дизассемблер)
                warriorInstructionTextView.text =
                    "${instruction.opcode} ${instruction.operandA}, ${instruction.operandB}"
                warriorInstructionPointerTextView.text =
                    warrior.taskQueue.first.instructionPointer.toString()
            }
        }

        fun setTextColor(color: Int) {
            warriorNameTextView.setTextColor(color)
            warriorTaskTextView.setTextColor(color)
            warriorInstructionTextView.setTextColor(color)
            warriorInstructionPointerTextView.setTextColor(color)

            warriorTaskLabel.setTextColor(color)
            warriorInstructionPointerLabel.setTextColor(color)
        }

        fun removeDebugTextViews() {
            warriorTaskTextView.text = ""
            warriorInstructionTextView.text = ""
            warriorInstructionPointerTextView.text = ""

            warriorTaskLabel.text = ""
            warriorInstructionPointerLabel.text = ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.warrior_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(warriors != null) {
            holder.bind(warriors!!.elementAt(position))
        }
    }

    override fun getItemCount(): Int {
        return Warriors.size
    }
}