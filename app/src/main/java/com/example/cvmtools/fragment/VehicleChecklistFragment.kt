package com.example.cvmtools.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.example.cvmtools.adapter.ChecklistItemAdapter
import com.example.cvmtools.databinding.FragmentVehicleChecklistBinding
import com.example.cvmtools.model.ChecklistSection
import com.example.cvmtools.model.VehicleChecklistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior


class VehicleChecklistFragment : Fragment(), ChecklistItemAdapter.OnListCheckBoxListener {

    private val vehicleChecklistViewModel: VehicleChecklistViewModel by viewModels()

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

        setUpBottomSheet()
        binding.bottomSheetLayout.checklistItemsRecyclerView.setHasFixedSize(true)
        setUpFAB()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout.vehicleChecklistBottomSheetContent)
        bottomSheetBehavior.isDraggable = false
        binding.bottomSheetLayout.vehicleChecklistBottomSheetContent.setOnClickListener {
            openOrCloseBottomSheet()
        }
    }

    private fun openOrCloseBottomSheet() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            getAnimationForArrow(binding.bottomSheetLayout.checklistUpIcon, 180F, 0F, 200).start()
        }
        else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            getAnimationForArrow(binding.bottomSheetLayout.checklistUpIcon, 0F, 180F, 200).start()
        }
    }

    private fun setUpFAB() {
        binding.lightFAB.setOnClickListener {
           setChecklist(ChecklistSection.LIGHT)
        }

        binding.interiorFAB.setOnClickListener {
            setChecklist(ChecklistSection.INTERIOR)
        }

        binding.engineFAB.setOnClickListener {
            setChecklist(ChecklistSection.ENGINE)
        }

        binding.emergencyFAB.setOnClickListener {
            setChecklist(ChecklistSection.EMERGENCY)
        }

        binding.tireFAB.setOnClickListener {
            setChecklist(ChecklistSection.TIRE)
        }

        binding.fuelFAB.setOnClickListener {
            setChecklist(ChecklistSection.FUEL)
        }

        binding.cleanFAB.setOnClickListener {
            setChecklist(ChecklistSection.CLEAN)
        }

        binding.rearFAB.setOnClickListener {
            setChecklist(ChecklistSection.REAR)
        }
    }

    private fun setChecklist(section: ChecklistSection) {
        val list = vehicleChecklistViewModel.getList(section)
        vehicleChecklistViewModel.currentSection = section
        if (list != null) {
            binding.bottomSheetLayout.checklistTitle.text = "Checklist - ${vehicleChecklistViewModel.getTitleSection(section)}"
            binding.bottomSheetLayout.checklistItemsRecyclerView.visibility = View.VISIBLE
            binding.bottomSheetLayout.checklistItemsRecyclerView.adapter = ChecklistItemAdapter(list, this)
            binding.bottomSheetLayout.selectAreaText.visibility = View.GONE
        } else {
            // should not go here
            binding.bottomSheetLayout.checklistItemsRecyclerView.visibility = View.GONE
            binding.bottomSheetLayout.selectAreaText.visibility = View.VISIBLE
            binding.bottomSheetLayout.selectAreaText.text = "No checklist available"
        }
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED)
            getAnimationForArrow(binding.bottomSheetLayout.checklistUpIcon, 0F, 180F, 200).start()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCheckboxClick(position: Int, isChecked: Boolean) {
        vehicleChecklistViewModel.setItemChecked(position, isChecked)
    }

    private fun getAnimationForArrow(icon: ImageView, startRotation: Float, endRotation: Float, duration: Long): ValueAnimator {
        val anim = ValueAnimator.ofFloat(startRotation, endRotation)
        anim.addUpdateListener { valueAnimator ->
            val newRotationValue = valueAnimator.animatedValue as Float
            icon.rotation = newRotationValue
        }
        anim.duration = duration
        return anim
    }
}
