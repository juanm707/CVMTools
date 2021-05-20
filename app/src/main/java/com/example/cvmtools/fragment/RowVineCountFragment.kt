package com.example.cvmtools.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R
import com.example.cvmtools.adapter.BlockItemAdapter
import com.example.cvmtools.databinding.FragmentSecondBinding
import com.example.cvmtools.model.RowVineCountViewModel
import java.io.File
import java.io.FileOutputStream

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RowVineCountFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RowVineCountViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.blocksFinished.adapter = BlockItemAdapter(viewModel.blocks.value!!, viewModel.vineyard.value ?: "", viewModel.getBlockStatus())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add vineyards to drop down menu
        setupVineyardDropDown()

        setUpAbleToUpload()

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_rowVineCountFragment_to_selectTaskFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupVineyardDropDown() {

        val items = getVineyards()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        val autoCompleteTextView = (binding.vineyardDropdownMenu.editText as? AutoCompleteTextView)

        autoCompleteTextView?.setAdapter(adapter)
        autoCompleteTextView?.setOnItemClickListener { parent, view, position, id ->
            val vineyardSelected = adapter.getItem(position) ?: ""
            viewModel.setVineyard(vineyardSelected)
        }

        viewModel.vineyard.observe(viewLifecycleOwner, { newVineyard ->
            autoCompleteTextView?.setText(newVineyard, false)
            setUpAbleToUpload()
        })

    }

    private fun setUpAbleToUpload() {
        binding.uploadButton.isEnabled = viewModel.getUploadStatus()
        binding.uploadButton.setOnClickListener {
            onShare()
        }
    }

    private fun getVineyards(): List<String> {
        val a = context?.resources?.getStringArray(R.array.vineyards)
        if (a != null) {
            return a.toList()
        }
        return listOf()
    }

    private fun onShare() {
        //generate data
        val data = viewModel.onShareData()

        try {
            //saving the file into device
            val fileName = "${viewModel.vineyard.value}_Row_Vine_Counts.csv"
            val out: FileOutputStream = (requireContext().openFileOutput(fileName, Context.MODE_PRIVATE) ?: null) as FileOutputStream
            out.write(data.toString().toByteArray(Charsets.UTF_8))
            out.close()

            val fileLocation = File(requireContext().filesDir, fileName)
            val path: Uri = FileProvider.getUriForFile(requireContext(), "com.example.cvmtools.fileprovider", fileLocation)

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hello there, ${viewModel.vineyard.value} Row Vine Counts")
                putExtra(Intent.EXTRA_SUBJECT, "${viewModel.vineyard.value} Row Vine Counts")
                putExtra(Intent.EXTRA_STREAM, path)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "text/csv"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}