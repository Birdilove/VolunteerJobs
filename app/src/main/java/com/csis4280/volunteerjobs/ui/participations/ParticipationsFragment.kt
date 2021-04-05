package com.csis4280.volunteerjobs.ui.participations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.csis4280.volunteerjobs.R
import com.csis4280.volunteerjobs.databinding.FragmentParticipationsBinding
import com.csis4280.volunteerjobs.ui.home.ListAdapter
import kotlinx.android.synthetic.main.list_layout.*

class ParticipationsFragment : Fragment(), ListAdapter.ListItemListener {

    private lateinit var participationsViewModel: ParticipationsViewModel
    private lateinit var participationsBinding: FragmentParticipationsBinding
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        participationsBinding = FragmentParticipationsBinding.inflate(inflater, container, false)
        participationsViewModel = ViewModelProvider(this).get(ParticipationsViewModel::class.java)

        participationsViewModel.signedUpjobList?.observe(viewLifecycleOwner, Observer { signedUp ->
            participationsViewModel.jobList?.observe(viewLifecycleOwner, Observer{
                adapter = ListAdapter(signedUp,it, this)
                participationsBinding.recyclerViewParticipations.adapter = adapter
                participationsBinding.recyclerViewParticipations.layoutManager = LinearLayoutManager(activity)
            })
        })
        return participationsBinding.root
    }

    override fun editJob(noteId: Int, isSignedUp: Int, postedBy: String) {

    }
}