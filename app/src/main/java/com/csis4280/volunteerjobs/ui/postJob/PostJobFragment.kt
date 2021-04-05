package com.csis4280.volunteerjobs.ui.postJob

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.csis4280.volunteerjobs.R
import com.csis4280.volunteerjobs.URL
import com.csis4280.volunteerjobs.databinding.FragmentPostJobBinding
import com.csis4280.volunteerjobs.databinding.FragmentResetPasswordBinding
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.home.HomeFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PostJobFragment : Fragment() {

    private lateinit var postJobViewModel: PostJobViewModel
    private lateinit var binding: FragmentPostJobBinding
    private val args: PostJobFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private lateinit var startDate: Date
    private lateinit var endDate: Date
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentPostJobBinding.inflate(inflater, container, false)
        postJobViewModel = ViewModelProvider(this).get(PostJobViewModel::class.java)

        binding.button.setOnClickListener {
            postJobViewModel.currentJob.value?.jobTitle = binding.editTextTitle.text.toString()
            postJobViewModel.currentJob.value?.jobType = binding.editTextType.text.toString()
            postJobViewModel.currentJob.value?.jobDescription =
                binding.editTextJobDesc.text.toString()
            postJobViewModel.currentJob.value?.startDate = startDate.toString()
            postJobViewModel.currentJob.value?.endDate = endDate.toString()
            postJobViewModel.currentJob.value?.postedBy = auth.currentUser?.email.toString()
            postJobViewModel.updateJob()
            val action =
                PostJobFragmentDirections.actionNavigationPostToNavigationHome()
            findNavController().navigate(action)
        }

        binding.buttonStartDate.setOnClickListener {
            pickDateTime(binding.buttonStartDate,0)
        }

        binding.buttonEndDate.setOnClickListener {
            endDate = pickDateTime(binding.buttonEndDate,1)
        }

        binding.button4.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
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
        DatePickerDialog(requireContext(), { _, year, month, day ->
            pickedDateTime.set(year, month, day)
            val simpleDateFormat = SimpleDateFormat("ddd-MMM-yyyy")
            button.text = simpleDateFormat.format(pickedDateTime.time)
            if(flag == 0){
            startDate = pickedDateTime.time
            }else{
                endDate = pickedDateTime.time
            }
        }, startYear, startMonth, startDay).show()

        return pickedDateTime.time
    }
}