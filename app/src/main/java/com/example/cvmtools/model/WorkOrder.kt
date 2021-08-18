package com.example.cvmtools.model

enum class WorkOrderType {
    NONE, FUNGICIDE, HERBICIDE, INSECTICIDE, INJECTION, PESTICIDE
}

enum class UOM {
    NONE, GALLONS, POUNDS, FLUID_OUNCES, OUNCES
}

enum class ApplicationMethod {
    NONE, GROUND, AIR
}

class WorkOrder {

    var vineyard: String = ""
    var type: WorkOrderType = WorkOrderType.NONE
    var product: String = ""
    var date: Long = 0
    var jobCode: String = ""

    var blockAndAcres: MutableMap<String, Float> = mutableMapOf()

    var productList: MutableMap<String, ProductAmount> = mutableMapOf()

    var epaList: List<String> = mutableListOf()

    var tankSize: Float = 0F
    var tanks: Float = 0F
    var rei: Int = 0
    var applicationMethod = ApplicationMethod.NONE
    var specialInstructions: String = ""

    class ProductAmount (
        var amount: Float = 0F,
        var uom: UOM = UOM.NONE
    )
}

