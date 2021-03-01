package com.csis4280.volunteerjobs.ui.postJob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.csis4280.volunteerjobs.R

class PostJobFragment : Fragment() {

    private lateinit var postJobViewModel: PostJobViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        postJobViewModel =
                ViewModelProvider(this).get(PostJobViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_post_job, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        postJobViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}