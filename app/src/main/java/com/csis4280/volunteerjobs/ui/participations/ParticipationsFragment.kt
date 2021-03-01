package com.csis4280.volunteerjobs.ui.participations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.csis4280.volunteerjobs.R

class ParticipationsFragment : Fragment() {

    private lateinit var participationsViewModel: ParticipationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        participationsViewModel =
                ViewModelProvider(this).get(ParticipationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_participations, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        participationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}