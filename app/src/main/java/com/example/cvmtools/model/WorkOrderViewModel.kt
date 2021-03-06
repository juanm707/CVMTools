package com.example.cvmtools.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class WorkOrderViewModel : ViewModel() {
    private var workOrder = WorkOrder()

    private val _blocks = MutableLiveData<List<String>>(listOf())
    val blocks: LiveData<List<String>> = _blocks

    var companyName = "Martinez Vineyard Management"

    fun clearWorkOrder() {
        workOrder = WorkOrder()
        _blocks.value = listOf()
    }

    fun setVineyard(vineyardSelected: String) {
        workOrder.vineyard = vineyardSelected
        _blocks.value = getBlocksForVineyard(vineyardSelected)
    }

    fun setTankSize(size: String) {
        workOrder.tankSize = size.toFloat()
    }

    fun setSpecialInstructions(specialInstructions: String) {
        workOrder.specialInstructions = specialInstructions
    }

    fun setType(typeSelected: String) {
        when (typeSelected) {
            "None" -> workOrder.type = WorkOrderType.NONE
            "Fungicide" -> workOrder.type = WorkOrderType.FUNGICIDE
            "Herbicide" -> workOrder.type = WorkOrderType.HERBICIDE
            "Insecticide" -> workOrder.type = WorkOrderType.INSECTICIDE
            "Injection" -> workOrder.type = WorkOrderType.INJECTION
            "Pesticide" -> workOrder.type = WorkOrderType.PESTICIDE
        }

        setJobCode(workOrder.type)
        setApplicationMethod(workOrder.type)
    }

    fun setProduct(product: String) {
        workOrder.product = product
    }

    private fun setApplicationMethod(workOrderType: WorkOrderType) {
        when (workOrderType) {
            WorkOrderType.NONE -> workOrder.applicationMethod = ApplicationMethod.GROUND
            WorkOrderType.FUNGICIDE -> workOrder.applicationMethod = ApplicationMethod.GROUND
            WorkOrderType.HERBICIDE -> workOrder.applicationMethod = ApplicationMethod.GROUND
            WorkOrderType.INSECTICIDE -> workOrder.applicationMethod = ApplicationMethod.GROUND
            WorkOrderType.INJECTION -> workOrder.applicationMethod = ApplicationMethod.GROUND
            WorkOrderType.PESTICIDE -> workOrder.applicationMethod = ApplicationMethod.GROUND
        }
    }

    private fun setJobCode(workOrderType: WorkOrderType) {
        when (workOrderType) {
            WorkOrderType.NONE -> workOrder.jobCode = "000 None"
            WorkOrderType.FUNGICIDE -> workOrder.jobCode = "270 Fungicide Application"
            WorkOrderType.HERBICIDE -> workOrder.jobCode = "271 Herbicide"
            WorkOrderType.INSECTICIDE -> workOrder.jobCode = "272 Insecticide"
            WorkOrderType.INJECTION -> workOrder.jobCode = "035 Fertilize Inject."
            WorkOrderType.PESTICIDE -> workOrder.jobCode = "274 Pesticide"
        }
    }

    fun setApplicationDate(dateSelection: Long?) {
        if (dateSelection != null) {
            workOrder.date = dateSelection
        }
    }

    fun getApplicationDateFormatted(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(workOrder.date + 86400000))
    }

    fun getApplicationMethodString(): String {
        return when (workOrder.applicationMethod) {
            ApplicationMethod.GROUND -> "Ground"
            ApplicationMethod.AIR ->  "Air"
            ApplicationMethod.NONE -> "None"
        }
    }

    fun getProduct(): String {
        return workOrder.product
    }

    fun getTanksString(): String {
        return workOrder.tanks.toString()
    }

    fun getTankSizeString(): String {
        return "${workOrder.tankSize} gal"
    }

    fun getVineyard(): String {
        return workOrder.vineyard
    }

    fun getTypeAndProduct(): String {
        return "${getType(workOrder.type)} ${workOrder.product}"
    }

    fun getJobCode(): String {
        return workOrder.jobCode
    }

    private fun getType(type: WorkOrderType): String {
        return when (type) {
            WorkOrderType.NONE -> "None"
            WorkOrderType.FUNGICIDE -> "Fungicide"
            WorkOrderType.HERBICIDE -> "Herbicide"
            WorkOrderType.INSECTICIDE -> "Insecticide"
            WorkOrderType.INJECTION -> "Injection"
            WorkOrderType.PESTICIDE -> "Pesticide"
        }
    }

    fun getSpecialInstructions(): String {
        return workOrder.specialInstructions
    }

    fun addProduct(productName: String, productAmount: Float, uom: String): Boolean {
        // check if productName in map
        // if product is in
        if (workOrder.productList.containsKey(productName)) {

            val storedAmount = workOrder.productList[productName]?.amount
            val storedUom = workOrder.productList[productName]?.uom

            if (storedAmount == productAmount && storedUom == getUOMFromString(uom)) {
                // if *product already in* and amount is same
                return false
            } else {
                // if not change it
                workOrder.productList[productName]?.amount = productAmount
                workOrder.productList[productName]?.uom = getUOMFromString(uom)
                return true
            }
        } else {
            // does not exist so add
            workOrder.productList[productName] = WorkOrder.ProductAmount(productAmount, getUOMFromString(uom))
            return true
        }
    }

    fun addBlock(blockName: String): Boolean {
        return if (workOrder.blockAndAcres.containsKey(blockName)) {
            false
        } else {
            workOrder.blockAndAcres[blockName] = randomFloatAcres()
            true
        }
    }

    fun removeProduct(productName: String): Boolean {
        return if (workOrder.productList.containsKey(productName)) {
            workOrder.productList.remove(productName)
            true
        } else
            false
    }

    fun removeBlock(blockName: String): Boolean {
        return if (workOrder.blockAndAcres.containsKey(blockName)) {
            workOrder.blockAndAcres.remove(blockName)
            true
        } else
            false
    }

    private fun getUOMFromString(uom: String): UOM {
        return when (uom) {
            "None" -> UOM.NONE
            "Gallons" -> UOM.GALLONS
            "Pounds" -> UOM.POUNDS
            "Fluid Ounces" -> UOM.FLUID_OUNCES
            "Ounces" -> UOM.OUNCES
            else -> UOM.NONE
        }
    }

    private fun getStringFromUOM(uom: UOM): String {
        return when (uom) {
            UOM.NONE -> "None"
            UOM.GALLONS -> "Gallons"
            UOM.POUNDS -> "Pounds"
            UOM.FLUID_OUNCES -> "Fluid Ounces"
            UOM.OUNCES -> "Ounces"
        }
    }

    fun getProductList(): List<String> {
        return workOrder.productList.map { product ->
            product.key
        }
    }

    fun getProductAmountAndUOM(): List<String> {
        return workOrder.productList.map { product ->
            "${roundTwoDecimals(product.value.amount)} ${getStringFromUOM(product.value.uom)}"
        }
    }

    fun getProductNameAmountAndUOM(): List<String> {
        return workOrder.productList.map { product ->
            "${product.key} ${roundTwoDecimals(product.value.amount)} ${getStringFromUOM(product.value.uom)}"
        }
    }

    fun getBlockNamesAndAcres(): List<String> {
        return workOrder.blockAndAcres.map { block ->
            "${block.key} ${block.value} ac"
        }
    }

    fun getBlockNames(): List<String> {
        return workOrder.blockAndAcres.map { block ->
            block.key
        }
    }

    fun getBlockAcres(): List<String> {
        return workOrder.blockAndAcres.map { block ->
            "${block.value} ac"
        }
    }

    fun getEPAsForProducts(): MutableList<String> {
        return MutableList(workOrder.productList.size) { "1234-567-AA"}
    }

    private fun roundTwoDecimals(value: Float): String {
        return "%.2f".format(value)
    }

    private fun getBlocksForVineyard(vineyard: String): List<String> {
        return when (vineyard) {
            "Blanton" -> listOf("1", "2", "3")
            "Blueline" -> listOf("1", "2", "3", "4", "5", "6", "7", "8a", "8b", "9a", "9b", "10", "11")
            "Hourglass" -> listOf("1 Upper", "1 Lower", "2", "3")
            "Tournahu" -> listOf("1", "2", "3", "4", "5")
            else -> {
                listOf()
            }
        }
    }

    fun getTotalAcres(): Float {
        return workOrder.blockAndAcres.map { block -> block.value }.sum()
    }

    private fun randomFloatAcres(): Float {
        return (0..10).random().toFloat()
    }
}
