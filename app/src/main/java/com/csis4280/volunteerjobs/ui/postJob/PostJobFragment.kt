package com.csis4280.volunteerjobs.ui.postJob

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.csis4280.volunteerjobs.databinding.FragmentPostJobBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_post_job.*
import java.text.SimpleDateFormat
import java.util.*

class PostJobFragment : Fragment() {

    private lateinit var postJobViewModel: PostJobViewModel
    private lateinit var binding: FragmentPostJobBinding
    private val args: PostJobFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private lateinit var startDate: Date
    private lateinit var endDate: Date
    private var startDatecheck: String = "";
    private var endDatecheck: String = "";

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentPostJobBinding.inflate(inflater, container, false)
        postJobViewModel = ViewModelProvider(this).get(PostJobViewModel::class.java)

        binding.button.setOnClickListener {
            if ((editTextTitle.text.trim()
                    .isNotEmpty()) && ((startDatecheck.isNotEmpty())) && (endDatecheck.isNotEmpty()) && (binding.editTextType.text.toString()
                    .trim().isNotEmpty()) && (binding.editTextJobDesc.text.toString().trim()
                    .isNotEmpty()) && (startDate <= endDate)
            ) {
                postJobViewModel.currentJob.value?.jobTitle = binding.editTextTitle.text.toString()
                postJobViewModel.currentJob.value?.jobType = binding.editTextType.text.toString()
                postJobViewModel.currentJob.value?.jobDescription =
                    binding.editTextJobDesc.text.toString()
                postJobViewModel.currentJob.value?.startDate = startDate.toString()
                postJobViewModel.currentJob.value?.endDate = endDate.toString()
                postJobViewModel.currentJob.value?.postedBy = auth.currentUser?.email.toString()
                postJobViewModel.currentJob.value?.noOfSlots = binding.editTextNumber.text.toString().toInt()
                postJobViewModel.updateJob()
                navigate()
            } else {
                if ((editTextTitle.text.trim().isEmpty())) {
                    Toast.makeText(requireContext(), " Please enter the title", Toast.LENGTH_SHORT)
                        .show()
                    Log.i("ms", "checked")
                } else if ((startDatecheck.isEmpty()) || (endDatecheck.isEmpty())) {
                    Toast.makeText(
                        requireContext(),
                        " Please select the start date and end date",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (binding.editTextType.text.toString().trim().isEmpty()) {
                    Toast.makeText(requireContext(), " Please enter the type", Toast.LENGTH_SHORT)
                        .show()
                } else if (binding.editTextJobDesc.text.toString().trim().isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        " Please enter the description",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        " Please choose valid dates",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.buttonStartDate.setOnClickListener {
            pickDateTime(binding.buttonStartDate, 0)
        }

        binding.buttonEndDate.setOnClickListener {
            endDate = pickDateTime(binding.buttonEndDate, 1)
        }

        postJobViewModel.getJobById(args.jobid, auth.currentUser?.email.toString())
        postJobViewModel.getMaxId(auth.currentUser?.email.toString())

        return binding.root
    }

    private fun pickDateTime(button: Button, flag: Int): Date {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val pickedDateTime = Calendar.getInstance()
        var datePickerDialog: DatePickerDialog =
            DatePickerDialog(requireContext(), { _, year, month, day ->
                pickedDateTime.set(year, month, day)
                val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
                button.text = simpleDateFormat.format(pickedDateTime.time)
                if (flag == 0) {
                    startDate = pickedDateTime.time
                    startDatecheck = button.text.toString()
                } else {
                    endDate = pickedDateTime.time
                    endDatecheck = button.text.toString()
                }
            }, startYear, startMonth, startDay)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000;
        datePickerDialog.show()
        return pickedDateTime.time
    }

    private fun navigate(){
        val action = PostJobFragmentDirections.actionNavigationPostToNavigationHome()
        findNavController().navigate(action)
    }
}