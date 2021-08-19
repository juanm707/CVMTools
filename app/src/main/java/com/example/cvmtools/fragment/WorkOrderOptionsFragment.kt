package com.example.cvmtools.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R
import com.example.cvmtools.adapter.AlertDialogListAdapter
import com.example.cvmtools.databinding.FragmentWorkOrderOptionsBinding
import com.example.cvmtools.model.WorkOrderViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hendrix.pdfmyxml.PdfDocument
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer
import java.io.File

class WorkOrderOptionsFragment : Fragment() {

    private val workOrderViewModel: WorkOrderViewModel by viewModels()

    private val pageHeight = 2115
    private val pageWidth = 1500

    private var _binding: FragmentWorkOrderOptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWorkOrderOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpVineyardDropDown()
        setUpTypeDropDown()
        setUpUOMDropDown()
        setUpDateSelect()
        binding.generateWoButton.setOnClickListener {
            generateWorkOrderPDF()
        }
        setProductButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpVineyardDropDown() {

        val items = getVineyards()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        val autoCompleteTextView = (binding.vineyardDropdownMenu.editText as? AutoCompleteTextView)

        autoCompleteTextView?.setAdapter(adapter)
        autoCompleteTextView?.setOnItemClickListener { parent, view, position, id ->
            val vineyardSelected = adapter.getItem(position) ?: ""
            workOrderViewModel.setVineyard(vineyardSelected)
        }
    }

    private fun setUpTypeDropDown() {
        val items = getTypes()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        val autoCompleteTextView = (binding.workOrderType.editText as? AutoCompleteTextView)

        autoCompleteTextView?.setAdapter(adapter)
        autoCompleteTextView?.setOnItemClickListener { parent, view, position, id ->
            val typeSelected = adapter.getItem(position) ?: ""
            workOrderViewModel.setType(typeSelected)
        }
    }

    private fun setUpUOMDropDown()  {
        val items = getUOMTypes()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        val autoCompleteTextView = (binding.uom.editText as? AutoCompleteTextView)

        autoCompleteTextView?.setAdapter(adapter)
    }

    private fun setUpDateSelect() {
        binding.applicationDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select due date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, "Date Picker")

            datePicker.addOnPositiveButtonClickListener {
                if (datePicker.selection != null) {
                    workOrderViewModel.setApplicationDate(datePicker.selection)
                    binding.applicationDate.text = getString(R.string.application_date_is, workOrderViewModel.getApplicationDateFormatted())
                }
            }
        }
    }

    private fun getVineyards(): List<String> {
        val a = context?.resources?.getStringArray(R.array.vineyards)
        if (a != null) {
            return a.toList()
        }
        return listOf()
    }

    private fun getTypes(): List<String> {
        val a = context?.resources?.getStringArray(R.array.productTypes)
        if (a != null) {
            return a.toList()
        }
        return listOf()
    }

    private fun getUOMTypes(): List<String> {
        val a = context?.resources?.getStringArray(R.array.uomTypes)
        if (a != null) {
            return a.toList()
        }
        return listOf()
    }

    private fun setProductButtons() {

        setTextChangeListener(binding.productNameText, binding.productName, )
        setTextChangeListener(binding.productAmountText, binding.productAmount)
        
        binding.addProductButton.setOnClickListener { 
            addProductButtonOnClick()
        }

        binding.removeProductButton.setOnClickListener {
            removeProductButtonOnClick()
        }

        binding.viewProductsButton.setOnClickListener {
            viewProductButtonOnClick()
        }
    }

    private fun addProductButtonOnClick() {
        // check if name and amount and uom is not empty
        val name = isTextFieldEmpty(binding.productNameText, binding.productName, "Name")
        val amount = isTextFieldEmpty(binding.productAmountText, binding.productAmount, "Amount")

        // if name and amount present
        if (name && amount) {

            val productName = binding.productNameText.text.toString()
            val productAmount = binding.productAmountText.text.toString()
            val uom = binding.uomAutoComplete.text.toString()

            // returns true if added, false if duplicate, same amount, same name
            if (workOrderViewModel.addProduct(productName, productAmount.toFloat(), uom))
                showToast("$productName was added")
            else
                showToast("$productName already exists")
        }
    }

    private fun removeProductButtonOnClick() {
        val name = isTextFieldEmpty(binding.productNameText, binding.productName, "Name")
        if (name) {
            val productName = binding.productNameText.text.toString()
            // returns true if removed, false if not in list
            if (workOrderViewModel.removeProduct(productName))
                showToast("$productName was removed")
            else
                showToast("$productName is not in the list")
        }
    }

    private fun viewProductButtonOnClick() {
        MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
            .setNegativeButton("Dismiss") { dialog, which ->
                // Respond to negative button press
            }
            .setView(getListLayout(layoutInflater))
            .show()
    }

    private fun getListLayout(inflater: LayoutInflater): View? {
        val dialogLayout = inflater.inflate(R.layout.product_list_alert_dialog, null)
        val title = dialogLayout.findViewById<TextView>(R.id.title)
        title.text = getString(R.string.product)
        val recyclerView = dialogLayout.findViewById<RecyclerView>(R.id.list_wo_recycler_view)
        recyclerView.adapter = AlertDialogListAdapter(requireContext(), workOrderViewModel.getProductNameAmountAndUOM())
        return dialogLayout
    }

    private fun isTextFieldEmpty(textInputEditText: TextInputEditText, textInputLayout: TextInputLayout, objectRequired: String): Boolean {
        return if (textInputEditText.text.isNullOrBlank() || textInputEditText.text.isNullOrEmpty()) {
            textInputLayout.error = "$objectRequired required"
            false
        } else
            true
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    
    private fun setTextChangeListener(textInputEditText: TextInputEditText, textInputLayout: TextInputLayout) {
        textInputEditText.addTextChangedListener {
            // if error, when user starts typing, remove error
            textInputLayout.error = null
        }
    }

    private fun generateWorkOrderPDF() {

        workOrderViewModel.setProduct(binding.mainProductText.text.toString())
        workOrderViewModel.setTankSize(binding.tankSizeText.text.toString())
        workOrderViewModel.setSpecialInstructions(binding.specialInstructionsText.text.toString())

        val page: AbstractViewRenderer = object : AbstractViewRenderer(requireContext(), R.layout.work_order_final) {

            override fun initView(view: View) {
                // header
                view.findViewById<TextView>(R.id.companyName).text = workOrderViewModel.companyName
                view.findViewById<TextView>(R.id.typeAndProduct).text = workOrderViewModel.getTypeAndProduct()
                view.findViewById<TextView>(R.id.jobCode).text = workOrderViewModel.getJobCode()

                // date
                view.findViewById<TextView>(R.id.date).text = "Date for Application ${workOrderViewModel.getApplicationDateFormatted()}"

                // location and total acres
                view.findViewById<TextView>(R.id.location).text = "${workOrderViewModel.getVineyard()} Winery, Angwin, Napa County, CA 95408"
                view.findViewById<TextView>(R.id.totalAcres).text = "2.55 ac"

                // extra info
                view.findViewById<TextView>(R.id.tankSize).text = workOrderViewModel.getTankSizeString()
                view.findViewById<TextView>(R.id.tanks).text = workOrderViewModel.getTanksString()
                view.findViewById<TextView>(R.id.rei).text = "24 hrs"
                view.findViewById<TextView>(R.id.applicationMethod).text = workOrderViewModel.getApplicationMethodString()
                view.findViewById<TextView>(R.id.specialInstructions).text = workOrderViewModel.getSpecialInstructions()

                setWorkOrderLists(view)
            }

            private fun setWorkOrderLists(view: View) {
                // set block List
                setListView(listOf("02", "03", "04A"), view.findViewById(R.id.blockList))

                // set block area list
                setListView(listOf("1.09 ac", "0.18 ac", "0.53 ac"), view.findViewById(R.id.blockAreaList))

                // set product list
                setListView(workOrderViewModel.getProductList(), view.findViewById(R.id.productList))

                // set epa list
                setListView(workOrderViewModel.getEPAsForProducts(), view.findViewById(R.id.epaList))

                //set amount and uom list
                setListView(workOrderViewModel.getProductAmountAndUOM(), view.findViewById(R.id.purchasedProductList))
            }

            private fun setListView(list: List<String>, listView: ListView) {
                val adapter = ArrayAdapter(requireContext(), R.layout.block_list_wo_item, list)
                listView.adapter = adapter
            }
        }

        // you can reuse the bitmap if you want
        page.isReuseBitmap = true

        val doc = PdfDocument(requireContext())

        // add as many pages as you have
        doc.addPage(page)

        setPDFDocumentInfo(doc)

        doc.setListener(object : PdfDocument.Callback {
            override fun onComplete(file: File?) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete")

                if (file != null) {
                    val pdfUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().applicationContext.packageName.toString() + ".fileprovider",
                        file
                    )
                    val pdfIntent: Intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        setDataAndType(pdfUri, "application/pdf")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                    val chooser: Intent = Intent.createChooser(pdfIntent, null)
                    startActivity(chooser)
                }
            }

            override fun onError(e: Exception?) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Error")
            }
        })

        doc.createPdf(requireContext())
    }

    private fun setPDFDocumentInfo(doc: PdfDocument) {
        doc.setRenderWidth(pageWidth)
        doc.setRenderHeight(pageHeight)
        doc.orientation = PdfDocument.A4_MODE.PORTRAIT
        doc.setProgressTitle(R.string.app_name)
        doc.setProgressMessage(R.string.please_wait)
        doc.fileName = "${workOrderViewModel.getVineyard()}_${workOrderViewModel.getProduct()}_WO_${replaceSlashesFromDateString(workOrderViewModel.getApplicationDateFormatted())}"
        doc.setSaveDirectory(requireContext().getExternalFilesDir(null))
        doc.setInflateOnMainThread(false)
    }

    private fun replaceSlashesFromDateString(applicationDateFormatted: String): String {
        return applicationDateFormatted.replace('/', '-')
    }
}
