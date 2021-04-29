package com.example.cvmtools.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R
import com.example.cvmtools.fragment.RowVineCountFragment
import com.example.cvmtools.fragment.RowVineCountFragmentDirections

class BlockItemAdapter(private val blockNameList: List<String>, private val vineyard: String, private val blockStatus: MutableMap<String, Boolean>) : RecyclerView.Adapter<BlockItemAdapter.BlockItemViewHolder>() {

    class BlockItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val blockName: TextView = itemView.findViewById(R.id.block_name)
        val editButton: Button = itemView.findViewById(R.id.edit_button)
        val statusImage: ImageView = itemView.findViewById(R.id.status_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockItemViewHolder {
        val layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.block_item, parent, false)
        return BlockItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: BlockItemViewHolder, position: Int) {

        holder.blockName.text = blockNameList[position]

        if (blockStatus[blockNameList[position]] == true)
            holder.statusImage.setImageResource(R.drawable.ic_baseline_check_circle_24)
        else
            holder.statusImage.setImageResource(R.drawable.ic_baseline_highlight_off_24)

        holder.editButton.setOnClickListener { view ->
            val action = RowVineCountFragmentDirections.actionRowVineCountFragmentToEditRowVineCountFragment(
                vineyard = vineyard, block = holder.blockName.text.toString()
            )
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
       return  blockNameList.size
    }
}