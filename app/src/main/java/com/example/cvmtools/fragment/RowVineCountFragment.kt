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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add vineyards to drop down menu
        setupVineyardDropDown()

        setUpRecyclerView()

        setUpAbleToUpload()

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_rowVineCountFragment_to_selectTaskFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.blocksFinished
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.blocks.observe(viewLifecycleOwner, { newBlocks ->
            recyclerView.adapter = BlockItemAdapter(newBlocks, viewModel.vineyard.value
                    ?: "", viewModel.getBlockStatus())
        })
        recyclerView.setHasFixedSize(true)
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
            out.write(data.toString().toByteArray())
            out.close()

            //exporting
            val context: Context = requireContext()
            val fileLocation = File(requireContext().filesDir, fileName)
            val path: Uri = FileProvider.getUriForFile(context, "com.example.cvmtools", fileLocation)

            val fileIntent = Intent(Intent.ACTION_SEND)
            fileIntent.type = "text/csv"
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "${viewModel.vineyard.value} Row Vine Counts")
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            fileIntent.putExtra(Intent.EXTRA_STREAM, path)

            startActivity(Intent.createChooser(fileIntent, "Send mail"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}