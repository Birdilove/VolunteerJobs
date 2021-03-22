package com.csis4280.volunteerjobs.ui.postJob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.csis4280.volunteerjobs.R
import com.csis4280.volunteerjobs.databinding.FragmentPostJobBinding
import com.csis4280.volunteerjobs.databinding.FragmentResetPasswordBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PostJobFragment : Fragment() {

    private lateinit var postJobViewModel: PostJobViewModel
    private lateinit var binding: FragmentPostJobBinding
    private val args: PostJobFragmentArgs by navArgs()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPostJobBinding.inflate(inflater,container, false)
        postJobViewModel = ViewModelProvider(this).get(PostJobViewModel::class.java)

        postJobViewModel.currentJob.observe(viewLifecycleOwner,{

        })

        binding.button.setOnClickListener{
            postJobViewModel.currentJob.value?.jobTitle = binding.editTextTitle.text.toString()
            postJobViewModel.currentJob.value?.jobType = binding.editTextType.text.toString()
            postJobViewModel.currentJob.value?.jobDescription = binding.editTextJobDesc.text.toString()
            postJobViewModel.currentJob.value?.startDate = SimpleDateFormat("dd-MM-yyyy").parse("14-02-2018")
            postJobViewModel.currentJob.value?.endDate = SimpleDateFormat("dd-MM-yyyy").parse("14-02-2018")
            postJobViewModel.updateJob()
        }

        postJobViewModel.getJobById(args.jobid)
        return binding.root
    }
}