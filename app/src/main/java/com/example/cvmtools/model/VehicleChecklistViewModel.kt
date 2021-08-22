package com.example.cvmtools.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VehicleChecklistViewModel : ViewModel() {

    private var allChecklists = mutableMapOf<ChecklistSection, ChecklistItemList>()

    var currentSection = ChecklistSection.LIGHT

    private var comment: String = ""

    init {
        initializeAllChecklists()
    }

    fun getList(section: ChecklistSection): ChecklistItemList? {
        return allChecklists[section]
    }

    fun setItemChecked(position: Int, checked: Boolean) {
        val currentList = getList(currentSection)
        if (currentList != null)
            currentList.itemList[position].checked = checked
    }

    fun getTitleSection(section: ChecklistSection): String {
        return when (section) {
            ChecklistSection.LIGHT -> "Lights"
            ChecklistSection.INTERIOR -> "Interior"
            ChecklistSection.ENGINE -> "Mechanical"
            ChecklistSection.EMERGENCY -> "Emergency"
            ChecklistSection.TIRE -> "Tires"
            ChecklistSection.FUEL -> "Fuel"
            ChecklistSection.CLEAN -> "Body"
            ChecklistSection.REAR -> "Rear"
        }
    }

    fun setComment(newComment: String) {
        comment = newComment
    }

    fun getComment(): String {
        return comment
    }

    fun getChecklistShareData(): StringBuilder {
        // return the data to be shared for csv file
        val data = StringBuilder()
        data.append("Section,Item,Checked")
        allChecklists.forEach { (section, checkList) ->
            checkList.itemList.forEach { item ->
                data.append("\n$section,${item.itemName},${item.checked}")
            }
        }
        return data
    }

    private fun initializeAllChecklists() {
        initializeLightChecklist()
        initializeInteriorChecklist()
        initializeEngineChecklist()
        initializeEmergencyChecklist()
        initializeTireChecklist()
        initializeFuelChecklist()
        initializeCleanChecklist()
        initializeRearChecklist()
    }

    private fun initializeLightChecklist() {
        val section = ChecklistSection.LIGHT
        val list = mutableListOf(
            ChecklistItem("Head/Stop Lights"),
            ChecklistItem("Tail/Dash Lights"),
            ChecklistItem("Turn Indicators"),
            ChecklistItem("Clearance/Marker")
        )

        allChecklists[section] = ChecklistItemList(list)
    }

    private fun initializeInteriorChecklist() {
        val section = ChecklistSection.INTERIOR
        val list = mutableListOf(
            ChecklistItem("Defroster/Heater"),
            ChecklistItem("Horn"),
            ChecklistItem("Mirrors"),
            ChecklistItem("Oil Pressure"),
            ChecklistItem("Steering"),
            ChecklistItem("Windshield Wipers"),
        )

        allChecklists[section] = ChecklistItemList(list)
    }

    private fun initializeEngineChecklist() {
        val section = ChecklistSection.ENGINE
        val list = mutableListOf(
            ChecklistItem("Air Compressor"),
            ChecklistItem("Air Lines"),
            ChecklistItem("Battery"),
            ChecklistItem("Belts and Hoses"),
            ChecklistItem("Clutch"),
            ChecklistItem("Engine"),
            ChecklistItem("Exhaust Front"),
            ChecklistItem("Fluid Levels"),
            ChecklistItem("Radiator"),
            ChecklistItem("Starter"),
            ChecklistItem("Transmission")
        )

        allChecklists[section] = ChecklistItemList(list)
    }

    private fun initializeEmergencyChecklist() {
        val section = ChecklistSection.EMERGENCY
        val list = mutableListOf(
            ChecklistItem("Fire Extinguisher"),
            ChecklistItem("Flags/Flares/Fuses"),
            ChecklistItem("Reflective Triangles"),
            ChecklistItem("Spare Bulbs and Fuses"),
            ChecklistItem("Spare Seal Beam")
        )

        allChecklists[section] = ChecklistItemList(list)
    }

    private fun initializeTireChecklist() {
        val section = ChecklistSection.TIRE
        val list = mutableListOf(
            ChecklistItem("Brake Accessories"),
            ChecklistItem("Brake - Parking"),
            ChecklistItem("Brake - Service"),
            ChecklistItem("Front Axle"),
            ChecklistItem("Tire Chains"),
            ChecklistItem("Tires"),
            ChecklistItem("Wheels and Rims")
        )

        allChecklists[section] = ChecklistItemList(list)
    }

    private fun initializeFuelChecklist() {
        val section = ChecklistSection.FUEL
        val list = mutableListOf(
            ChecklistItem("Fuel Tanks")
        )

        allChecklists[section] = ChecklistItemList(list)
    }

    private fun initializeCleanChecklist() {
        val section = ChecklistSection.CLEAN
        val list = mutableListOf(
            ChecklistItem("Body"),
            ChecklistItem("Frame and Assembly"),
            ChecklistItem("Reflectors"),
            ChecklistItem("Suspension System"),
        )

        allChecklists[section] = ChecklistItemList(list)
    }

    private fun initializeRearChecklist() {
        val section = ChecklistSection.REAR
        val list = mutableListOf(
            ChecklistItem("Exhaust Back"),
            ChecklistItem("Fifth Wheel"),
            ChecklistItem("Rear End"),
            ChecklistItem("Trunk/Storage"),
        )

        allChecklists[section] = ChecklistItemList(list)
    }
}
