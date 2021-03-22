package com.csis4280.volunteerjobs.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.csis4280.volunteerjobs.R
import com.csis4280.volunteerjobs.databinding.FragmentHomeBinding
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.database.user
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(), ListAdapter.ListItemListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: ListAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        auth = FirebaseAuth.getInstance()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.jobList?.observe(viewLifecycleOwner, Observer { allJobsList ->
            homeViewModel.signedUpjobList?.observe(viewLifecycleOwner, {
                adapter = ListAdapter(allJobsList, it, this)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
            })
        })
        homeViewModel.getJobById(0)
        homeViewModel.getUserByEmail(auth.currentUser?.email.toString())
        return binding.root
    }

    override fun editJob(jobId: Int, isSignedUp: Int) {
        val action = HomeFragmentDirections.actionNavigationHomeToJobDetailsFragment(jobId, isSignedUp)
        findNavController().navigate(action)
    }

    override fun onItemSelectionChanged() {
        /*Log.i("Key", homeViewModel.currentJob.value?.jobId!!.toString())
        if(homeViewModel.currentJob.value?.jobId!!>0) {
            homeViewModel.addToParticipation(homeViewModel.currentJob.value?.jobId!!, homeViewModel.currentUser.value?.userId!!, auth.currentUser?.email.toString())
        }*/
    }
}