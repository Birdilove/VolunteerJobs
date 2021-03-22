package com.csis4280.volunteerjobs.ui.jobDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.csis4280.volunteerjobs.databinding.FragmentJobDetailsBinding
import com.google.firebase.auth.FirebaseAuth

class JobDetailsFragment : Fragment() {

    private lateinit var viewModel: JobDetailsViewModel
    private lateinit var binding: FragmentJobDetailsBinding
    private val args: JobDetailsFragmentArgs by navArgs()
    lateinit var auth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentJobDetailsBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(JobDetailsViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        viewModel.currentJob.observe(viewLifecycleOwner,{ job ->
            viewModel.signedUpjobList?.observe(viewLifecycleOwner,{
                binding.textViewType.text = job.jobType
                binding.titleText.text = job.jobTitle
                binding.textViewDateFrom.text = job.startDate.time.toString() +" "+ job.endDate
                binding.textView3.text = job.jobDescription
            })
        })
        viewModel.getJobById(args.jobId)
        viewModel.getUserByEmail(auth.currentUser?.email.toString())
        viewModel.getParticipantById(args.jobId)

        if(args.isSignedUp == 1){
            binding.buttonSignForJob.text = "Sign Out"
        }
        binding.buttonSignForJob.setOnClickListener{
            if(args.isSignedUp == 1){
                viewModel.deleteJob()
                binding.buttonSignForJob.text = "Sign In"
            }
            else{
                add()
                binding.buttonSignForJob.text = "Sign Out"
            }
        }
        return binding.root
    }

    fun add(){
        viewModel.addToParticipation(viewModel.currentJob.value?.jobId!!, viewModel.currentUser.value?.userId!!, auth.currentUser?.email.toString())
    }
}