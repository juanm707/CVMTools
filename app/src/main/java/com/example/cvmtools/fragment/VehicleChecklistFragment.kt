package com.example.cvmtools.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cvmtools.adapter.ChecklistItemAdapter
import com.example.cvmtools.databinding.FragmentVehicleChecklistBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior


class VehicleChecklistFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var _binding: FragmentVehicleChecklistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVehicleChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout.vehicleChecklistBottomSheetContent)
        bottomSheetBehavior.isDraggable = false
        binding.bottomSheetLayout.checklistItemsRecyclerView.setHasFixedSize(true)
        binding.bottomSheetLayout.checklistItemsRecyclerView.adapter = ChecklistItemAdapter()

        binding.bottomSheetLayout.vehicleChecklistBottomSheetContent.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            else
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
