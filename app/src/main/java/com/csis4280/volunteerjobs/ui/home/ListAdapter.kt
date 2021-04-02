package com.csis4280.volunteerjobs.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.csis4280.volunteerjobs.R
import com.csis4280.volunteerjobs.databinding.ListLayoutBinding
import com.csis4280.volunteerjobs.ui.database.job

class ListAdapter(
    private val jobList: List<job>,
    private val selectedJobList: List<job>,
    private val listener: ListItemListener
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding = ListLayoutBinding.bind(itemView)
    }

    interface ListItemListener {
        fun editJob(noteId: Int, isSignedUp: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobList[position]
        with(holder.binding) {
            jobTitleText.text = job.jobTitle
            jobTypeText.text = job.jobType
            startDate.text = "From: " + job.startDate.toString()

            var isSignedUp = 0
            if (selectedJobList.contains(job)) {
                isSignedUp = 1;
            } else {
                isSignedUp = 0;
            }

            root.setOnClickListener {
                listener.editJob(job.jobId, isSignedUp)
            }
        }
    }

    override fun getItemCount() = jobList.size
}

