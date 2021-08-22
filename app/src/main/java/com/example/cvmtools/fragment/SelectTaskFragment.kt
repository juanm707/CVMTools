package com.example.cvmtools.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cvmtools.R
import com.example.cvmtools.databinding.FragmentFirstBinding
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException


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

        binding.workOrderPdfGenerator.setOnClickListener {
            findNavController().navigate(R.id.action_selectTaskFragment_to_workOrderOptionsFragment)
        }

        binding.vehicleInspectionButton.setOnClickListener {
            findNavController().navigate(R.id.action_selectTaskFragment_to_vehicleChecklistFragment)
        }

        binding.plantLabelingButton.setOnClickListener {
            findNavController().navigate(R.id.action_selectTaskFragment_to_diseaseImageLabelingFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
