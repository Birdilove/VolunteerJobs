package com.csis4280.volunteerjobs.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.csis4280.volunteerjobs.IOnBackPressed
import com.csis4280.volunteerjobs.URL
import com.csis4280.volunteerjobs.databinding.FragmentHomeBinding
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.postJob.PostJobViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.Job
import org.json.JSONObject
import java.net.URISyntaxException

class HomeFragment : Fragment(), ListAdapter.ListItemListener, IOnBackPressed {
    private lateinit var postJobViewModel: PostJobViewModel
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
        postJobViewModel = ViewModelProvider(this).get(PostJobViewModel::class.java)

        homeViewModel.jobList?.observe(viewLifecycleOwner, Observer { allJobsList ->
            homeViewModel.signedUpjobList?.observe(viewLifecycleOwner, {
                Log.i("SelectedListSize ", homeViewModel.signedUpjobList?.value?.size.toString())
                adapter = ListAdapter(allJobsList, it, this)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
            })
        })

        homeViewModel.getJobById(0, auth.currentUser?.email.toString())
        homeViewModel.getUserByEmail(auth.currentUser?.email.toString())

        return binding.root
    }

    override fun editJob(jobId: Int, isSignedUp: Int, postedBy: String) {
        val action =
            HomeFragmentDirections.actionNavigationHomeToJobDetailsFragment(jobId, isSignedUp, postedBy)
        findNavController().navigate(action)
    }

    override fun onBackPressed() {
        requireActivity().finish()
    }
}