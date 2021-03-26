package com.csis4280.volunteerjobs.ui.jobDetails

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.csis4280.volunteerjobs.databinding.FragmentJobDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class JobDetailsFragment : Fragment() {

    private lateinit var viewModel: JobDetailsViewModel
    private lateinit var binding: FragmentJobDetailsBinding
    private val args: JobDetailsFragmentArgs by navArgs()
    lateinit var auth: FirebaseAuth
    private lateinit var startDate: Date
    private lateinit var endDate: Date

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(JobDetailsViewModel::class.java)
        val simpleDateFormat = SimpleDateFormat("ddd-MMM-yyyy")
        viewModel.currentJob.observe(viewLifecycleOwner, { job ->
            viewModel.signedUpjobList?.observe(viewLifecycleOwner, {
                binding.textViewListedBy.text = "Listed by: " + job.postedBy
                binding.title.setText(job.jobTitle)
                binding.type.setText(job.jobType)
                binding.buttonUpdateStartDate.text = simpleDateFormat.format(job.startDate)
                binding.buttonUpdateEndDate.text = simpleDateFormat.format(job.endDate)
                Toast.makeText(requireContext(), simpleDateFormat.format(job.endDate), Toast.LENGTH_SHORT).show()
                binding.description.setText(job.jobDescription)
                if (job.postedBy == auth.currentUser?.email.toString()) {
                    Log.i("Focusable", "Focus")
                    binding.title.focusable = View.FOCUSABLE
                    binding.type.focusable = View.FOCUSABLE
                    binding.buttonUpdateStartDate.focusable = View.FOCUSABLE
                    binding.buttonUpdateEndDate.focusable = View.FOCUSABLE
                    binding.description.focusable = View.FOCUSABLE
                    binding.buttonDeleteJob.isClickable = true
                    binding.buttonUpdateJob.isClickable = true
                } else {
                    binding.title.focusable = View.NOT_FOCUSABLE
                    binding.type.focusable = View.NOT_FOCUSABLE
                    binding.buttonUpdateStartDate.focusable = View.NOT_FOCUSABLE
                    binding.buttonUpdateEndDate.focusable = View.NOT_FOCUSABLE
                    binding.description.focusable = View.NOT_FOCUSABLE
                    binding.buttonDeleteJob.isClickable = false
                    binding.buttonUpdateJob.isClickable = false
                }
            })
        })
        viewModel.getJobById(args.jobId)
        viewModel.getParticipantById(args.jobId, auth.currentUser?.email.toString())
        viewModel.getUserByEmail(auth.currentUser?.email.toString())
        var isSignedUp: Int = args.isSignedUp
        if (isSignedUp == 1) {
            binding.buttonSignForJob.text = "Sign Out"
        }
        binding.buttonSignForJob.setOnClickListener {
            if (isSignedUp == 1) {
                viewModel.signOutOfJob()
                binding.buttonSignForJob.text = "Sign Up"
                isSignedUp = 0
            } else {
                add()
                binding.buttonSignForJob.text = "Sign Out"
                isSignedUp = 1
            }
        }

        binding.buttonUpdateJob.setOnClickListener {
            viewModel.currentJob.observe(viewLifecycleOwner, {
                it.jobTitle = binding.title.text.toString()
                it.jobType = binding.type.text.toString()
                it.jobDescription = binding.description.text.toString()
                it.startDate = startDate
                it.endDate = endDate
            })
            viewModel.updateJob()
            Toast.makeText(requireContext(), "Job post updated", Toast.LENGTH_SHORT).show()
        }

        binding.buttonDeleteJob.setOnClickListener {
            viewModel.deleteJob()
            Toast.makeText(requireContext(), "Job post deleted", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.buttonUpdateStartDate.setOnClickListener {
            startDate = pickDateTime(binding.buttonUpdateStartDate)
        }

        binding.buttonUpdateEndDate.setOnClickListener {
            endDate = pickDateTime(binding.buttonUpdateEndDate)
        }

        return binding.root
    }

    private fun add() {
        viewModel.addToParticipation(
            viewModel.currentJob.value?.jobId!!,
            viewModel.currentUser.value?.userId!!,
            auth.currentUser?.email.toString()
        )
    }

    private fun pickDateTime(@Nullable button: Button): Date {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val pickedDateTime = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            pickedDateTime.set(year, month, day)
            val simpleDateFormat = SimpleDateFormat("ddd-MMM-yyyy")
            button.text = simpleDateFormat.format(pickedDateTime.time)
        }, startYear, startMonth, startDay).show()
        return pickedDateTime.time
    }
}