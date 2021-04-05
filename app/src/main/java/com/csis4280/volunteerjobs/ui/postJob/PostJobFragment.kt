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
import kotlinx.android.synthetic.main.fragment_post_job.*
import kotlinx.android.synthetic.main.list_layout.*
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
    private var startDatecheck: String="";
    private var endDatecheck: String="";

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentPostJobBinding.inflate(inflater, container, false)
        postJobViewModel = ViewModelProvider(this).get(PostJobViewModel::class.java)

        binding.button.setOnClickListener {

                Log.i("msg",PostJobFragment::endDate.isLateinit.toString())


            if((editTextTitle.text.trim().length>0) && ((startDatecheck.length > 0)) && (endDatecheck.length > 0) && (binding.editTextType.text.toString().trim().length>0) && (binding.editTextJobDesc.text.toString().trim().length>0) &&( startDate <= endDate) ){
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
            else{
                 if ((editTextTitle.text.trim().length<=0))
                 {
                     Toast.makeText(requireContext(), " Please enter the title", Toast.LENGTH_SHORT).show()
                     Log.i("ms","checked")
                 }
               else if ((startDatecheck.length <= 0) || (endDatecheck.length <= 0))
                {
                    Toast.makeText(requireContext(), " Please select the start date and end date", Toast.LENGTH_SHORT).show()
                }
                else if(binding.editTextType.text.toString().trim().length<=0){
                     Toast.makeText(requireContext(), " Please enter the type", Toast.LENGTH_SHORT).show()
                 }
                 else if(binding.editTextJobDesc.text.toString().trim().length<=0){
                     Toast.makeText(requireContext(), " Please enter the description", Toast.LENGTH_SHORT).show()
                 }
                else{
                     Toast.makeText(requireContext(), " Please choose valid dates", Toast.LENGTH_SHORT).show()
                 }


            }



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
        var datePickerDialog : DatePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            pickedDateTime.set(year, month, day)
            val simpleDateFormat = SimpleDateFormat("ddd-MMM-yyyy")
            button.text = simpleDateFormat.format(pickedDateTime.time)
            Log.i(
                "DATE",
                simpleDateFormat.parse(simpleDateFormat.format(pickedDateTime.time)).toString()
            )
            if (flag == 0) {
                startDate = pickedDateTime.time
            } else {
                endDate = pickedDateTime.time
            }
        }, startYear, startMonth, startDay)
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show()
        /*
        DatePickerDialog(requireContext(), { _, year, month, day ->
            pickedDateTime.set(year, month, day)
            val simpleDateFormat = SimpleDateFormat("ddd-MMM-yyyy")
            button.text = simpleDateFormat.format(pickedDateTime.time)
            if(flag == 0){
            startDate = pickedDateTime.time
                startDatecheck = pickedDateTime.time.toString()
            }else{
                endDate = pickedDateTime.time
                endDatecheck = pickedDateTime.time.toString()
            }
        }, startYear, startMonth, startDay).show()
*/
        return pickedDateTime.time
    }
}