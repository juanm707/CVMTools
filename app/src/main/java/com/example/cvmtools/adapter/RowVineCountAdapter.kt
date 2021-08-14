package com.example.cvmtools.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R

class RowVineCountAdapter(private val rowCounts: MutableMap<Int, Int>, private val context: Context) : RecyclerView.Adapter<RowVineCountAdapter.RowVineCountItemViewHolder>() {

    private val mapKeys = rowCounts.keys
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onDeleteItem(position: Int, rowNumber: Int)
    }

    class RowVineCountItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rowNumber: TextView = itemView.findViewById(R.id.row_number)
        val vineCount: TextView = itemView.findViewById(R.id.vine_count)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowVineCountItemViewHolder {
        val layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_vine_count_item, parent, false)
        return RowVineCountItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RowVineCountItemViewHolder, position: Int) {
        val key = mapKeys.elementAt(position)
        holder.rowNumber.text = context.getString(R.string.row_hashtag, key.toString())
        holder.vineCount.text = context.resources.getQuantityString(R.plurals.numberOfVineCounts, rowCounts[key]!!, rowCounts[key]!!)
        holder.deleteButton.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                mListener.onDeleteItem(position, key)
            }
        }
    }

    override fun getItemCount(): Int {
        return rowCounts.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }
}