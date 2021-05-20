package com.example.cvmtools.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cvmtools.R
import java.text.FieldPosition

class RowVineCountViewModel : ViewModel() {

    private val _vineyard = MutableLiveData<String>("")
    val vineyard: LiveData<String> = _vineyard

    private val _blocks = MutableLiveData<List<String>>(listOf())
    val blocks: LiveData<List<String>> = _blocks

    private val _rowCount = MutableLiveData<MutableMap<Int, Int>>(mutableMapOf()) // current row vine count for fragment
    val rowCount: LiveData<MutableMap<Int, Int>> = _rowCount

    private var blockRowCounts: MutableMap<String, BlockRowCount> = mutableMapOf() // all row vine counts
    private var blockStatus: MutableMap<String, Boolean> = mutableMapOf()
    private var ableToUpload = false
    private var position: Int = 1

    fun setVineyard(vineyard: String) {
        if (_vineyard.value != vineyard) {
            resetBlockCounts()
            _vineyard.value = vineyard
            setBlocks(getBlocksForVineyard())
        }
    }

    private fun resetBlockCounts() {
        _rowCount.value = mutableMapOf()
        blockRowCounts = mutableMapOf()
        blockStatus = mutableMapOf()
        ableToUpload = false
    }

    private fun setBlocks(blocks: List<String>) {
        _blocks.value = blocks
    }

    fun addRowVineCount(row: Int, count: Int) {
        var newMap = _rowCount.value?.toMutableMap()
        if (newMap != null) {
            newMap[row] = count
            newMap = newMap.toSortedMap()
        }
        _rowCount.value = newMap
        position = (_rowCount.value?.toList()?.indexOf(Pair(row, count)) ?: _rowCount.value?.size) as Int
    }

    fun saveRowVineCount(block: String) {
        if (!ableToUpload)
            ableToUpload = true

        blockRowCounts[block] = BlockRowCount(_rowCount.value!!)
        blockStatus[block] = true
    }

    private fun getBlocksForVineyard(): List<String> {
        return when (vineyard.value) {
            "Blanton" -> listOf("1", "2", "3")
            "Blueline" -> listOf("1", "2", "3", "4", "5", "6", "7", "8a", "8b", "9a", "9b", "10", "11")
            "Hourglass" -> listOf("1 Upper", "1 Lower", "2", "3")
            "Tournahu" -> listOf("1", "2", "3", "4", "5")
            else -> {
                listOf()
            }
        }
    }

    fun setRowVineCounts(block: String) {
        val blockCounts = blockRowCounts[block]
        if (blockCounts != null)
            _rowCount.value = blockCounts.rowVineCounts
        else
            _rowCount.value = mutableMapOf()
    }

    fun getLastPosition(): Int {
        return position
    }

    fun getBlockStatus(): MutableMap<String, Boolean> {
        return blockStatus
    }

    fun getUploadStatus(): Boolean {
        blockStatus.forEach { (_, value) ->
            if (value) {
                return true
            }
        }
        return false
    }

    fun setFalseBlockStatus(block: String) {
        blockStatus[block] = false
    }

    fun removeRowVineCount(key: Int) {
        val newMap = _rowCount.value?.toMutableMap()
        newMap?.remove(key)
        _rowCount.value = newMap
    }

    fun onShareData(): StringBuilder {
        // return the data to be shared for csv file
        val data = StringBuilder()
        data.append("Block,Row,VineCount")
        blockRowCounts.forEach { (block, rowCount) ->
            rowCount.rowVineCounts.forEach { (row, count) ->
                data.append("\n$block,$row,$count")
            }
        }
        return data
    }

    private class BlockRowCount(val rowVineCounts: MutableMap<Int, Int>) {

    }
}