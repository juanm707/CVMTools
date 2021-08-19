package com.example.cvmtools.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R

class ChecklistItemAdapter : RecyclerView.Adapter<ChecklistItemAdapter.ChecklistItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistItemViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.checklist_item, parent, false)
        return ChecklistItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ChecklistItemViewHolder, position: Int) {
        holder.checklistItem.text = "Lights"
    }

    override fun getItemCount(): Int {
        return 10
    }

    class ChecklistItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checklistItem: TextView = itemView.findViewById(R.id.checklistItem)
    }

}


