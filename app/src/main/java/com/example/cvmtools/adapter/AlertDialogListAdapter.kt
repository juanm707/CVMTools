package com.example.cvmtools.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R

class AlertDialogListAdapter(private val context: Context, private val list: List<String>) : RecyclerView.Adapter<AlertDialogListAdapter.AlertDialogListItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlertDialogListItemViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.block_list_wo_item, parent, false)
        return AlertDialogListItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: AlertDialogListItemViewHolder, position: Int) {
        val item = list[position]
        holder.text.text = item
        holder.text.textSize = 16F
        holder.text.setPadding((16 * context.resources.displayMetrics.density).toInt(), 0, 0, (8 * context.resources.displayMetrics.density).toInt())
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AlertDialogListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.item)
    }
}
