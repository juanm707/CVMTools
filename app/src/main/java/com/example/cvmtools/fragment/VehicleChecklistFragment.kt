package com.example.cvmtools.fragment

import android.animation.ValueAnimator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.cvmtools.R
import com.example.cvmtools.adapter.AlertDialogListAdapter
import com.example.cvmtools.adapter.ChecklistItemAdapter
import com.example.cvmtools.databinding.FragmentVehicleChecklistBinding
import com.example.cvmtools.model.ChecklistSection
import com.example.cvmtools.model.VehicleChecklistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class VehicleChecklistFragment : Fragment(), ChecklistItemAdapter.OnListCheckBoxListener {

    private val vehicleChecklistViewModel: VehicleChecklistViewModel by viewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var commentEditText: EditText

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBottomSheet()
        binding.bottomSheetLayout.checklistItemsRecyclerView.setHasFixedSize(true)
        setUpFAB()
        setUpScrollViewListener()
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpScrollViewListener() {
        binding.vehicleScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                getAnimationForArrow(binding.bottomSheetLayout.checklistUpIcon, 180F, 0F, 200).start()
            }
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

        binding.submitFAB.setOnClickListener {
            viewCommentDialog()
        }

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

    private fun viewCommentDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
            .setPositiveButton("Send") { dialog, which ->
                vehicleChecklistViewModel.setComment(commentEditText.text.toString())
                shareChecklist()
            }
            .setNegativeButton("Dismiss") { dialog, which ->
                // Respond to negative button press
            }
            .setView(getListLayout(layoutInflater))
            .show()
    }

    private fun getListLayout(inflater: LayoutInflater): View? {
        val dialogLayout = inflater.inflate(R.layout.checklist_submit_alert_dialog, null)
        commentEditText = dialogLayout.findViewById(R.id.commentTextInputEditText)
        return dialogLayout
    }

    private fun shareChecklist() {
        //generate data
        val data = vehicleChecklistViewModel.getChecklistShareData()

        try {
            //saving the file into device
            val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.US)
            sdf.timeZone = TimeZone.getDefault()
            val date = sdf.format(Date())

            val fileName = "Vehicle_Checklist_$date.csv"
            val out: FileOutputStream = (requireContext().openFileOutput(fileName, Context.MODE_PRIVATE) ?: null) as FileOutputStream
            out.write(data.toString().toByteArray(Charsets.UTF_8))
            out.close()

            val fileLocation = File(requireContext().filesDir, fileName)
            val path: Uri = FileProvider.getUriForFile(requireContext(), "com.example.cvmtools.fileprovider", fileLocation)

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hello, here is the vehicle checklist for $date\nComment: ${vehicleChecklistViewModel.getComment()}")
                putExtra(Intent.EXTRA_SUBJECT, "$date Vehicle Checklist")
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
