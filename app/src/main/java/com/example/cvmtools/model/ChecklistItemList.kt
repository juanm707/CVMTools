package com.example.cvmtools.model

enum class ChecklistSection {
    LIGHT, INTERIOR, ENGINE, EMERGENCY, TIRE, FUEL, CLEAN, REAR
}

class ChecklistItemList(
    val itemList: MutableList<ChecklistItem>
)

class ChecklistItem(
    var itemName: String,
    var checked: Boolean = false
)
