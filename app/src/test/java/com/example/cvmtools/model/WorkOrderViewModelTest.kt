package com.example.cvmtools.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cvmtools.getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WorkOrderViewModelTest {

    private lateinit var workOrderViewModel: WorkOrderViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpViewModel() {
        workOrderViewModel = WorkOrderViewModel()
    }

    @Test
    fun clearWorkOrder_emptyBlocks_isEmpty() {
        // Given
        workOrderViewModel.addBlock("Example")

        // When
        workOrderViewModel.clearWorkOrder()

        // Then
        val value = workOrderViewModel.blocks.getOrAwaitValue()
        assertThat(value.isEmpty(), `is`(true))
    }

    @Test
    fun getTankSizeString_OneHundredGallons_returnsOneHundredGal() {
        // Given

        val tankSize = "100"
        workOrderViewModel.setTankSize(tankSize)

        // When
        val tankSizeString = workOrderViewModel.getTankSizeString()

        // Then
        assertThat(tankSizeString, `is`("100.0 gal"))
    }

    @Test
    fun getApplicationDateFormatted_timeZero_returnsJanFirstNineteenSeventy() {
        // Given
        workOrderViewModel.setApplicationDate(0) // Jan 1, 1970

        // When
        val date = workOrderViewModel.getApplicationDateFormatted()

        // Then
        assertThat(date, `is`("01/01/1970"))
    }

    @Test
    fun getApplicationDateFormatted_timeGoogleBirthday_returnsJanFirstNineteenSeventy() {
        // Given
        workOrderViewModel.setApplicationDate(906793200000)

        // When
        val date = workOrderViewModel.getApplicationDateFormatted()

        // Then
        assertThat(date, `is`("09/27/1998"))
    }

    @Test
    fun getTypeAndProduct_FungicideRhyme_returnsFungicideSpaceRhyme() {
        // Given
        val fungicide = "Fungicide"
        workOrderViewModel.setType(fungicide)
        val product = "Rhyme"
        workOrderViewModel.setProduct(product)

        // When
        val typeAndProduct = workOrderViewModel.getTypeAndProduct()

        // Then
        assertThat(typeAndProduct, `is`("Fungicide Rhyme"))
    }

    @Test
    fun addProduct_newProduct_returnsTrue() {
        // Given

        // When
        val result = workOrderViewModel.addProduct("Example Product", 1.0F, "Ounces")

        // Then
        assertThat(result, `is`(true))
    }

    @Test
    fun addProduct_duplicateProduct_returnFalse() {
        // Given
        val insert = workOrderViewModel.addProduct("Example Product", 1.0F, "Ounces")

        // When
        val result = workOrderViewModel.addProduct("Example Product", 1.0F, "Ounces")

        // Then
        assertThat(insert, `is`(true)) // first insert is true since new
        assertThat(result, `is`(false)) // second insert is false since duplicate
    }

    @Test
    fun addProduct_updateProduct_returnTrue() {
        // Given
        val insert = workOrderViewModel.addProduct("Example Product", 1.0F, "Ounces")

        // When
        val result = workOrderViewModel.addProduct("Example Product", 5.0F, "Gallons")

        // Then
        assertThat(insert, `is`(true)) // first insert is true since new
        assertThat(result, `is`(true)) // second insert is true since update
    }

    @Test
    fun addBlock_newBlock_returnsTrue() {
        // Given

        // When
        val insert = workOrderViewModel.addBlock("Example")

        // Then
        assertThat(insert, `is`(true)) // first insert is true since new
    }

    @Test
    fun addBlock_sameBlock_returnsFalse() {
        // Given
        val insert = workOrderViewModel.addBlock("Example")

        // When
        val result = workOrderViewModel.addBlock("Example")

        // Then
        assertThat(insert, `is`(true)) // first insert is true since new
        assertThat(result, `is`(false)) // second insert is false since duplicate
    }

    @Test
    fun removeProduct_productDoesNotExist_returnsFalse() {
        // Given

        // When
        val result = workOrderViewModel.removeProduct("Example")

        // Then
        assertThat(result, `is`(false))
    }

    @Test
    fun removeProduct_productDoesExist_returnsTrue() {
        // Given
        workOrderViewModel.addProduct("Example", 1.0F, "Gallons")

        // When
        val result = workOrderViewModel.removeProduct("Example")

        // Then
        assertThat(result, `is`(true))
    }

    @Test
    fun removeBlock_blockDoesNotExist_returnsFalse() {
        // Given

        // When
        val result = workOrderViewModel.removeBlock("Example")

        // Then
        assertThat(result, `is`(false))
    }

    @Test
    fun removeBlock_blockDoesExist_returnsTrue() {
        // Given
        workOrderViewModel.addBlock("Example")

        // When
        val result = workOrderViewModel.removeBlock("Example")

        // Then
        assertThat(result, `is`(true))
    }

    @Test
    fun getProductList_oneItem_returnsTrue() {
        // Given
        workOrderViewModel.addProduct("Example", 1.0F, "Gallons")

        // When
        val productList = workOrderViewModel.getProductList()

        // Then
        assertThat(productList.first(), `is`("Example"))
    }

    @Test
    fun getProductAmountAndUOM_oneProduct_returnsOneGallons() {
        // Given
        workOrderViewModel.addProduct("Example", 1.0F, "Gallons")

        // When
        val productList = workOrderViewModel.getProductAmountAndUOM()

        // Then
        assertThat(productList.first(), `is`("1.00 Gallons"))
    }

    @Test
    fun getProductAmountAndUOM_noProduct_returnsEmpty() {
        // Given

        // When
        val productList = workOrderViewModel.getProductAmountAndUOM()

        // Then
        assertThat(productList.isEmpty(), `is`(true))
    }

    @Test
    fun getProductNameAmountAndUOM_oneProduct_returnsExampleOneGallons() {
        // Given
        workOrderViewModel.addProduct("Example", 1.0F, "Gallons")

        // When
        val productList = workOrderViewModel.getProductNameAmountAndUOM()

        // Then
        assertThat(productList.first(), `is`("Example 1.00 Gallons"))
    }

    @Test
    fun getProductNameAmountAndUOM_noProduct_returnsEmpty() {
        // Given

        // When
        val productList = workOrderViewModel.getProductNameAmountAndUOM()

        // Then
        assertThat(productList.isEmpty(), `is`(true))
    }

    @Test
    fun getBlockNamesAndAcres_oneBlock_returnsExampleAcres() {
        // Given
        workOrderViewModel.addBlock("Example")

        // When
        val blockList = workOrderViewModel.getBlockNamesAndAcres()

        // Then
        assertThat(blockList.first().contains("Example"), `is`(true))
    }

    @Test
    fun getBlockNamesAndAcres_noBlocks_returnsEmpty() {
        // Given

        // When
        val blockList = workOrderViewModel.getBlockNamesAndAcres()

        // Then
        assertThat(blockList.isEmpty(), `is`(true))
    }

    @Test
    fun getBlockNames_exampleBlock_returnsExample() {
        // Given
        workOrderViewModel.addBlock("Example")

        // When
        val blockList = workOrderViewModel.getBlockNames()

        // Then
        assertThat(blockList.first(), `is`("Example"))
    }

    @Test
    fun getBlockNames_noBlocks_returnsEmpty() {
        // Given

        // When
        val blockList = workOrderViewModel.getBlockNames()

        // Then
        assertThat(blockList.isEmpty(), `is`(true))
    }

    @Test
    fun getBlockAcres_oneAcreEntry_returnsFloat() {
        // Given
        workOrderViewModel.addBlock("Example")

        // When
        val blockList = workOrderViewModel.getBlockAcres()

        // Then
        assertThat(blockList.first().contains("ac"), `is`(true))
    }

    @Test
    fun getBlockAcres_noEntry_returnsEmpty() {
        // Given

        // When
        val blockList = workOrderViewModel.getBlockAcres()

        // Then
        assertThat(blockList.isEmpty(), `is`(true))
    }

    @Test
    fun getEPAsForProducts() {
        // Given
        workOrderViewModel.addProduct("Example", 1.0F, "Gallons")

        // When
        val epaList = workOrderViewModel.getEPAsForProducts()

        // Then
        assertThat(epaList.size, `is`(1))
        assertThat(epaList.first(), `is`("1234-567-AA"))
    }
}
