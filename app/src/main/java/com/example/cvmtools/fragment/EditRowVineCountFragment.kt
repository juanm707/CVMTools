package com.example.cvmtools.fragment

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R
import com.example.cvmtools.adapter.RowVineCountAdapter
import com.example.cvmtools.databinding.FragmentEditRowVineCountBinding
import com.example.cvmtools.model.RowVineCountViewModel
import com.google.android.material.textfield.TextInputEditText

class EditRowVineCountFragment : Fragment(), TextView.OnEditorActionListener {

    companion object {
        const val BLOCK = "block"
    }

    private var _binding: FragmentEditRowVineCountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RowVineCountViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var block: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            block = it.getString(BLOCK).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditRowVineCountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUserInputEnter()
        setUpRecyclerView()
        setUpSaveButton()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (isUserInputEmpty())
                Toast.makeText(requireContext(), "Missing field", Toast.LENGTH_SHORT).show()
            else {
                viewModel.addRowVineCount(Integer.parseInt(binding.rowNumberEdit.text.toString()), Integer.parseInt(binding.rowCountEdit.text.toString()))
                recyclerView.scrollToPosition(viewModel.getLastPosition()) // scroll to last added
                viewModel.setFalseBlockStatus(block) // modify so need to save again
            }
            return true
        }
        return false
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.rowVineCountRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.setRowVineCounts(block)
        viewModel.rowCount.observe(viewLifecycleOwner, { rowCounts ->
            val newAdapter = RowVineCountAdapter(rowCounts, requireContext())
            newAdapter.setOnItemClickListener(object: RowVineCountAdapter.OnItemClickListener {
                override fun onDeleteItem(position: Int, rowNumber: Int) {
                    viewModel.removeRowVineCount(rowNumber)
                    viewModel.setFalseBlockStatus(block)
                    Toast.makeText(requireContext(), "Deleted Row #$rowNumber", Toast.LENGTH_SHORT).show()
                }
            })
            recyclerView.adapter = newAdapter

        })
        recyclerView.setHasFixedSize(true)
    }

    private fun handleUserInputEnter() {
        setInputEditor(binding.rowNumberEdit)
        setInputEditor(binding.rowCountEdit)

        binding.rowNumberEdit.addTextChangedListener {
            binding.rowNumberInput.error = null
        }

        binding.rowCountEdit.addTextChangedListener {
            binding.vineCountInput.error = null
        }
    }

    private fun isUserInputEmpty(): Boolean {
        var error = false
        if (binding.rowNumberEdit.text.isNullOrBlank()) {
            binding.rowNumberInput.error = "Required"
            error = true
        }
        if (binding.rowCountEdit.text.isNullOrBlank()) {
            binding.vineCountInput.error = "Required"
            error = true
        }
        return error
    }

    private fun setUpSaveButton() {
        binding.saveButton.setOnClickListener {
            viewModel.saveRowVineCount(block)
            Toast.makeText(requireContext(), "Successfully saved row vine counts for Block: $block", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editRowVineCountFragment_to_rowVineCountFragment)
        }
    }

    private fun setInputEditor(textInputEditor: TextInputEditText) {
        textInputEditor.setOnEditorActionListener(this)
    }
}