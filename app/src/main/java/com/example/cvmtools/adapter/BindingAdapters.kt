package com.example.cvmtools.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.adapter.BlockItemAdapter

@BindingAdapter("recyclerData", "vineyardName", "blockStatus")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<String>, vineyardName: String, blockStatus: MutableMap<String, Boolean>) {
    recyclerView.adapter = BlockItemAdapter(data, vineyardName, blockStatus)
    recyclerView.setHasFixedSize(true)
}
