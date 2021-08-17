package com.example.cvmtools.fragment

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cvmtools.R
import com.example.cvmtools.databinding.FragmentFirstBinding
import com.hendrix.pdfmyxml.PdfDocument.TAG_PDF_MY_XML
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SelectTaskFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val pageHeight = 2115
    private val pageWidth = 1500

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rowVineCountButton.setOnClickListener {
            findNavController().navigate(R.id.action_selectTaskFragment_to_rowVineCountFragment)
        }

        binding.wordOrderPdfGenerator.setOnClickListener {
            generateWorkOrderPDF()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateWorkOrderPDF() {

        val page: AbstractViewRenderer = object : AbstractViewRenderer(requireContext(), R.layout.work_order_final) {
            private var _text: String? = null
            fun setText(text: String?) {
                _text = text
            }

            override fun initView(view: View) {
                view.findViewById<TextView>(R.id.companyName).text = "Martinez Vineyard Management"
                view.findViewById<TextView>(R.id.typeAndProduct).text = "Fungicide S + Agrimek SC"
                view.findViewById<TextView>(R.id.jobCode).text = "270 Fungicide Application"

                view.findViewById<TextView>(R.id.date).text = "Date for Application 05/06/2021"

                view.findViewById<TextView>(R.id.location).text = "Outpost Winery, Angwin, Napa County, CA 95408"
                view.findViewById<TextView>(R.id.totalAcres).text = "2.55 ac"

                view.findViewById<TextView>(R.id.tankSize).text = "100.00 gal"
                view.findViewById<TextView>(R.id.tanks).text = "2.55"
                view.findViewById<TextView>(R.id.rei).text = "24 hrs"
                view.findViewById<TextView>(R.id.applicationMethod).text = "Ground"
                view.findViewById<TextView>(R.id.specialInstructions).text = ""

                val blockAdapter = ArrayAdapter(requireContext(), R.layout.block_list_wo_item, listOf("02", "03", "04A"))
                val blockList = view.findViewById<ListView>(R.id.blockList)
                blockList.adapter = blockAdapter

                val blockAreaAdapter = ArrayAdapter(requireContext(), R.layout.block_list_wo_item, listOf("1.09 ac", "0.18 ac", "0.53 ac"))
                val blockAreaList = view.findViewById<ListView>(R.id.blockAreaList)
                blockAreaList.adapter = blockAreaAdapter

                val productAdapter = ArrayAdapter(requireContext(), R.layout.block_list_wo_item, listOf("Water", "Sulfur DF", "Agrimek SC", "Sylcoat"))
                val productList = view.findViewById<ListView>(R.id.productList)
                productList.adapter = productAdapter

                val epaAdapter = ArrayAdapter(requireContext(), R.layout.block_list_wo_item, listOf("", "51036-352-AA-2935", "100-1351-ZA", "2935-50189-AA"))
                val epaList = view.findViewById<ListView>(R.id.epaList)
                epaList.adapter = epaAdapter

                val purchasedProductAdapter = ArrayAdapter(requireContext(), R.layout.block_list_wo_item, listOf("50.00 Gallons", "4.00 Pounds", "2.50 Fluid Ounces", "1.00 Fluid Ounces"))
                val purchasedProductList = view.findViewById<ListView>(R.id.purchasedProductList)
                purchasedProductList.adapter = purchasedProductAdapter
            }
        }

        // you can reuse the bitmap if you want
        page.isReuseBitmap = true

        val doc = com.hendrix.pdfmyxml.PdfDocument(requireContext())

        // add as many pages as you have
        doc.addPage(page)

        doc.setRenderWidth(pageWidth)
        doc.setRenderHeight(pageHeight)
        doc.orientation = com.hendrix.pdfmyxml.PdfDocument.A4_MODE.PORTRAIT
        doc.setProgressTitle(R.string.app_name)
        doc.setProgressMessage(R.string.vineyard)
        doc.fileName = "test3"
        doc.setSaveDirectory(requireContext().getExternalFilesDir(null))
        doc.setInflateOnMainThread(false)
        doc.setListener(object : com.hendrix.pdfmyxml.PdfDocument.Callback {
            override fun onComplete(file: File?) {
                Log.i(TAG_PDF_MY_XML, "Complete")

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
                Log.i(TAG_PDF_MY_XML, "Error")
            }
        })

        doc.createPdf(requireContext())



//        val doc = android.graphics.pdf.PdfDocument()
//
////        val bmp = BitmapFactory.decodeResource(resources, R.drawable.calculator)
////        val scaledBmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)
////
////        val paint = Paint()
////        val title = Paint()
//
//        val cachePath = File(requireContext().externalCacheDir, "work_orders/")
//        cachePath.mkdirs()
//
//        val file = File(cachePath, "WO_PDF.pdf")
//        val fileOutputStream = FileOutputStream(file)
//
//        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
//        val page = doc.startPage(pageInfo)

        //val canvas = page.canvas
        //canvas.drawBitmap(scaledBmp, 56F, 40F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
//        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
//
//        // below line is used for setting text size
//        // which we will be displaying in our PDF file.
//        title.textSize = 15F
//
//        // below line is sued for setting color
//        // of our text inside our PDF file.
//        title.color = ContextCompat.getColor(requireContext(), R.color.primaryColorVariant)
//
//        // below line is used to draw text in our PDF file.
//        // the first parameter is our text, second parameter
//        // is position from start, third parameter is position from top
//        // and then we are passing our variable of paint which is title.
//        canvas.drawText("Martinez Vineyard Management", 50F, 80F, title)
//        title.textAlign = Paint.Align.RIGHT
//
//        canvas.drawText("Fungicide S + Agrimek SC", pageWidth - 50F, 80F, title)
//
//        title.color = ContextCompat.getColor(requireContext(), R.color.black)
//        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
//        title.textAlign = Paint.Align.LEFT
//        canvas.drawText("Work Order", 50F, 100F, title)
//        title.textAlign = Paint.Align.RIGHT
//        canvas.drawText("270 Fungicide Application", pageWidth - 50F, 100F, title)
//
//        // similarly we are creating another text and in this
//        // we are aligning this text to center of our PDF file.
//        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
//        title.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
//        title.textSize = 15F;
//
//        // below line is used for setting
//        // our text to center of PDF.
//        title.textAlign = Paint.Align.CENTER
//        canvas.drawText("This is sample document which we have created.",
//            (pageWidth/2).toFloat(), (pageHeight/2).toFloat(), title)
//
//        val radius = 4f * resources.displayMetrics.density
//
//        val paintRectStroke = Paint()
//        paintRectStroke.color = ContextCompat.getColor(requireContext(), R.color.work_order_grey_border)
//        paintRectStroke.style = Paint.Style.STROKE
//        paintRectStroke.strokeWidth = 2f
//        paintRectStroke.strokeCap = Paint.Cap.ROUND;
//
//        val paintRectFill = Paint()
//        paintRectFill.color = ContextCompat.getColor(requireContext(), R.color.work_order_grey_fill)
//        paintRectFill.style = Paint.Style.FILL
//
//        val corners = floatArrayOf(
//            radius, radius,   // Top left radius in px
//            radius, radius,   // Top right radius in px
//            radius, radius,     // Bottom right radius in px
//            radius, radius      // Bottom left radius in px
//        )
//
//        val rect = RectF(50F, 120F, (pageWidth - 50).toFloat(), 220F)
//
//        val path = Path()
//        path.addRoundRect(rect, corners, Path.Direction.CW)
//
//        canvas.drawPath(path, paintRectFill)
//        canvas.drawPath(path, paintRectStroke)

        //doc.setRenderWidth(1500)
//        doc.setRenderHeight(2115)

//        val content = layoutInflater.inflate(R.layout.invoice, null)
//        content.layoutParams = ViewGroup.LayoutParams(pageWidth, pageHeight)
//        content.measure(pageWidth, pageHeight)
//        content.layout(0, 0,0, 0)
//
//        content.draw(page.canvas)
//        doc.finishPage(page)
//        doc.writeTo(fileOutputStream)
//        fileOutputStream.flush()
//        fileOutputStream.close()
//        doc.close()
//
//        val pdfUri = FileProvider.getUriForFile(
//            requireContext(),
//            requireContext().applicationContext.packageName.toString() + ".fileprovider",
//            file
//        )
//        val pdfIntent: Intent = Intent().apply {
//            action = Intent.ACTION_VIEW
//            setDataAndType(pdfUri, "application/pdf")
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
//
//        val chooser: Intent = Intent.createChooser(pdfIntent, null)
//        startActivity(chooser)


//        PdfGenerator.getBuilder()
//            .setContext(requireContext())
//            .fromLayoutXMLSource()
//            .fromLayoutXML(R.layout.work_order) /* "fromLayoutXML()" takes array of layout resources.
//			 * You can also invoke "fromLayoutXMLList()" method here which takes list of layout resources instead of array. */
//            .setFileName("Test-PDF") /* It is file name */
//            .setFolderName("cvmtoolsworkorders") /* It is folder name. If you set the folder name like this pattern (FolderA/FolderB/FolderC), then
//			 * FolderA creates first.Then FolderB inside FolderB and also FolderC inside the FolderB and finally
//			 * the pdf file named "Test-PDF.pdf" will be store inside the FolderB. */
//            .openPDFafterGeneration(true) /* It true then the generated pdf will be shown after generated. */
//            .build(object : PdfGeneratorListener() {
//                override fun onFailure(failureResponse: FailureResponse) {
//                    super.onFailure(failureResponse)
//                    /* If pdf is not generated by an error then you will find out the reason behind it
//				 * from this FailureResponse. */
//                }
//
//                override fun onStartPDFGeneration() {
//                    /*When PDF generation begins to start*/
//                }
//
//                override fun onFinishPDFGeneration() {
//                    /*When PDF generation is finished*/
//                }
//
//                override fun showLog(log: String) {
//                    super.showLog(log)
//                    /*It shows logs of events inside the pdf generation process*/
//                }
//
//                override fun onSuccess(response: SuccessResponse) {
//                    super.onSuccess(response)
//                    /* If PDF is generated successfully then you will find SuccessResponse
//				 * which holds the PdfDocument,File and path (where generated pdf is stored)*/
//                }
//            })

    }
}