package com.example.cvmtools.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R
import com.example.cvmtools.model.ChecklistItemList
import com.google.android.material.checkbox.MaterialCheckBox

class ChecklistItemAdapter(private val list: ChecklistItemList, private val checkBoxListener: OnListCheckBoxListener) : RecyclerView.Adapter<ChecklistItemAdapter.ChecklistItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistItemViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.checklist_item, parent, false)
        return ChecklistItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ChecklistItemViewHolder, position: Int) {
        val item = list.itemList[position]
        holder.checklistItem.text = item.itemName
        holder.checklistBox.isChecked = item.checked
        holder.checklistBox.setOnCheckedChangeListener { buttonView, isChecked ->
            checkBoxListener.onCheckboxClick(holder.adapterPosition, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return list.itemList.size
    }

    class ChecklistItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checklistItem: TextView = itemView.findViewById(R.id.checklistItem)
        val checklistBox: MaterialCheckBox = itemView.findViewById(R.id.checklistBox)
    }

    interface OnListCheckBoxListener {
        fun onCheckboxClick(position: Int, isChecked: Boolean) {}
    }
}
