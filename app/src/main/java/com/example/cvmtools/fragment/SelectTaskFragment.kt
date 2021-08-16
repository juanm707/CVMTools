package com.example.cvmtools.fragment

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cvmtools.R
import com.example.cvmtools.databinding.FragmentFirstBinding
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SelectTaskFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val pageHeight = 1120
    private val pageWidth = 792

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
        println("Hello World")
        val doc = android.graphics.pdf.PdfDocument()

        val bmp = BitmapFactory.decodeResource(resources, R.drawable.calculator)
        val scaledBmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

        val paint = Paint()
        val title = Paint()

        val cachePath = File(requireContext().externalCacheDir, "work_orders/")
        cachePath.mkdirs()

        val file = File(cachePath, "WO_PDF.pdf")
        val fileOutputStream = FileOutputStream(file)

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = doc.startPage(pageInfo)

        val canvas = page.canvas
        //canvas.drawBitmap(scaledBmp, 56F, 40F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15F

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(requireContext(), R.color.primaryColorVariant)

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("Martinez Vineyard Management", 50F, 80F, title)
        title.textAlign = Paint.Align.RIGHT

        canvas.drawText("Fungicide S + Agrimek SC", pageWidth - 50F, 80F, title)

        title.color = ContextCompat.getColor(requireContext(), R.color.black)
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        title.textAlign = Paint.Align.LEFT
        canvas.drawText("Work Order", 50F, 100F, title)
        title.textAlign = Paint.Align.RIGHT
        canvas.drawText("270 Fungicide Application", pageWidth - 50F, 100F, title)

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
        title.textSize = 15F;

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.",
            (pageWidth/2).toFloat(), (pageHeight/2).toFloat(), title)

        val radius = 4f * resources.displayMetrics.density

        val paintRectStroke = Paint()
        paintRectStroke.color = ContextCompat.getColor(requireContext(), R.color.work_order_grey_border)
        paintRectStroke.style = Paint.Style.STROKE
        paintRectStroke.strokeWidth = 2f
        paintRectStroke.strokeCap = Paint.Cap.ROUND;

        val paintRectFill = Paint()
        paintRectFill.color = ContextCompat.getColor(requireContext(), R.color.work_order_grey_fill)
        paintRectFill.style = Paint.Style.FILL

        val corners = floatArrayOf(
            radius, radius,   // Top left radius in px
            radius, radius,   // Top right radius in px
            radius, radius,     // Bottom right radius in px
            radius, radius      // Bottom left radius in px
        )

        val rect = RectF(50F, 120F, (pageWidth - 50).toFloat(), 220F)

        val path = Path()
        path.addRoundRect(rect, corners, Path.Direction.CW)

        canvas.drawPath(path, paintRectFill)
        canvas.drawPath(path, paintRectStroke)

//        val content =
//        content.draw(page.canvas)
        doc.finishPage(page)
        doc.writeTo(fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        doc.close()

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






//        val page: AbstractViewRenderer = object : AbstractViewRenderer(requireContext(), R.layout.work_order) {
//            private var _text: String? = null
//            fun setText(text: String?) {
//                _text = text
//            }
//
//            override fun initView(view: View) {
//                view.findViewById<TextView>(R.id.companyName).text = "Martinez Vineyard Management"
//            }
//        }
//
//        // you can reuse the bitmap if you want
//        page.isReuseBitmap = true
//
//        val doc = PdfDocument(requireContext())
//
//        // add as many pages as you have
//        doc.addPage(page)
//
//        doc.setRenderWidth(1500)
//        doc.setRenderHeight(2115)
//        doc.orientation = PdfDocument.A4_MODE.PORTRAIT
//        doc.setProgressTitle(R.string.app_name)
//        doc.setProgressMessage(R.string.vineyard)
//        doc.fileName = "test3"
//        doc.setSaveDirectory(requireContext().getExternalFilesDir(null))
//        doc.setInflateOnMainThread(false)
//        doc.setListener(object : PdfDocument.Callback {
//            override fun onComplete(file: File?) {
//                Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete")
//
//                if (file != null) {
//                    val pdfUri = FileProvider.getUriForFile(
//                        requireContext(),
//                        requireContext().applicationContext.packageName.toString() + ".fileprovider",
//                        file
//                    )
//                    val pdfIntent: Intent = Intent().apply {
//                        action = Intent.ACTION_VIEW
//                        setDataAndType(pdfUri, "application/pdf")
//                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    }
//
//                    val chooser: Intent = Intent.createChooser(pdfIntent, null)
//                    startActivity(chooser)
//                }
//            }
//
//            override fun onError(e: Exception?) {
//                Log.i(PdfDocument.TAG_PDF_MY_XML, "Error")
//            }
//        })
//
//        doc.createPdf(requireContext())
    }
}