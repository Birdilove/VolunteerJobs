package com.csis4280.volunteerjobs.ui.postJob

import android.app.Application
import androidx.lifecycle.*
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.csis4280.volunteerjobs.ui.database.job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostJobViewModel(app: Application): AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val currentJob = MutableLiveData<job>()

    fun getJobById(jobId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val book =
                        if (jobId != 0) {
                            database?.jobDao()?.getJobById(jobId)
                        } else {
                            job()
                        }
                currentJob.postValue(book!!)  // use postValue because it is running in the background
            }
        }
    }

    fun updateJob() {
        // would only be evaluated if currentNote is not null
        currentJob.value?.let {
            it.jobType = it.jobType.trim()
            it.jobDescription = it.jobDescription.trim()
            it.jobType = it.jobType.trim()
            if (it.jobId == 0 && it.jobTitle.isEmpty()) {
                return
            }

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it.jobTitle.isEmpty()) {
                        database?.jobDao()?.deleteJob(it)
                    } else {
                        // if it exists, it will perform an update
                        // see NoteDao for the implementation details
                        database?.jobDao()?.insertJob(it)
                    }
                }
            }
        }
    }
}