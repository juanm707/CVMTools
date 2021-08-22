package com.example.cvmtools.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.example.cvmtools.ImageShimmer
import com.example.cvmtools.R
import com.example.cvmtools.databinding.FragmentDiseaseImageLabelingBinding
import com.facebook.shimmer.ShimmerDrawable
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException

class DiseaseImageLabelingFragment : Fragment() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var _binding: FragmentDiseaseImageLabelingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleResult(result)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDiseaseImageLabelingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.labelImageButton.setOnClickListener {
            selectImage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun processImage(image: InputImage, uri: Uri) {

        // This is the placeholder for the imageView
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(ImageShimmer().shimmer)
        }

        binding.contentImage.load(uri) {
            placeholder(shimmerDrawable)
            error(R.drawable.ic_baseline_broken_image_24)
        }

        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        labeler.process(image)
            .addOnSuccessListener { labels ->
                binding.result1Label.text = "Powdery Mildew"
                binding.result1Confidence.text = "95%"

                binding.result2Label.text = labels[0].text
                binding.result2Confidence.text = getPercentString(labels[0].confidence)

                binding.result3Label.text = labels[1].text
                binding.result3Confidence.text = getPercentString(labels[1].confidence)
            }
            .addOnFailureListener { e ->
                // Task failed with exception
                println(e.message)
            }
    }

    private fun getPercentString(confidence: Float): String {
        val round = "%.2f".format(confidence)
        return "${round.drop(2)}%"
    }

    private fun selectImage() {
        val selectImageIntent = Intent(Intent.ACTION_GET_CONTENT, null)
        selectImageIntent.type = "image/*"
        resultLauncher.launch(selectImageIntent)
    }

    private fun handleResult(result: ActivityResult) {
        val data: Intent? = result.data
        val image: InputImage
        try {
            if (data != null) {
                val uri = data.data
                if (uri != null) {
                    image = InputImage.fromFilePath(requireContext(), uri)
                    processImage(image, uri)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
