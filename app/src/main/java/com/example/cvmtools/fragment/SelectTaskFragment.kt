package com.example.cvmtools.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cvmtools.R
import com.example.cvmtools.databinding.FragmentFirstBinding

class SelectTaskFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

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
            findNavController().navigate(R.id.action_selectTaskFragment_to_workOrderOptionsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
