package com.example.cvmtools.model

import org.junit.Assert.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class VehicleChecklistViewModelTest {

    @Test
    fun getCurrentSection_sectionFuel_isFuel() {
        // Given
        val vehicleChecklistViewModel = VehicleChecklistViewModel()
        vehicleChecklistViewModel.currentSection = ChecklistSection.FUEL
        // When
        val currentSect = vehicleChecklistViewModel.currentSection

        // Then
        assertThat(currentSect, `is`(ChecklistSection.FUEL))
    }

    @Test
    fun getList_fuelList_isOneSizeAndFirstItem() {
        // Given
        val vehicleChecklistViewModel = VehicleChecklistViewModel()

        // When
        val fuelList = vehicleChecklistViewModel.getList(ChecklistSection.FUEL)

        // Then... Fuel list is only 1 item, and first item is Fuel Tanks
        if (fuelList != null) {
            assertThat(fuelList.itemList.size, `is`(1))
            assertThat(fuelList.itemList[0].itemName, `is`("Fuel Tanks"))
        }
    }

    @Test
    fun setItemChecked_defaultSectionFirstItem_isTrue() {
        // Given
        val vehicleChecklistViewModel = VehicleChecklistViewModel() // def section is light
        val position = 0

        // When
        vehicleChecklistViewModel.setItemChecked(position, true)
        val currentList = vehicleChecklistViewModel.getList(vehicleChecklistViewModel.currentSection)

        // Then
        if (currentList != null) {
            assertThat(currentList.itemList[position].checked, `is`(true))
        }
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun setItemChecked_defaultSectionIndexOutOfBoundsLessThanZero_isException() {
        // Given
        val vehicleChecklistViewModel = VehicleChecklistViewModel() // def section is light
        val position = -1

        // When
        vehicleChecklistViewModel.setItemChecked(position, true)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun setItemChecked_defaultSectionIndexOutOfBoundsEqualListSize_is() {
        // Given
        val vehicleChecklistViewModel = VehicleChecklistViewModel() // def section is light
        val currentList = vehicleChecklistViewModel.getList(vehicleChecklistViewModel.currentSection)
        if (currentList != null) {
            val position = currentList.itemList.size

            // When
            vehicleChecklistViewModel.setItemChecked(position, true)
        }
    }
}
