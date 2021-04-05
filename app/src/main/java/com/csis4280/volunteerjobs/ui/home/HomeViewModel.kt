package com.csis4280.volunteerjobs.ui.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.csis4280.volunteerjobs.loggedInUser
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.database.participants
import com.csis4280.volunteerjobs.ui.database.user
import com.google.firebase.auth.FirebaseAuth
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val jobList = database?.jobDao()?.getAll()
    val signedUpjobList = database?.paeticipantDao()
        ?.getAllParticipations(loggedInUser)
    val currentJob = MutableLiveData<job>()
    val currentUser = MutableLiveData<user>()

    fun getJobById(jobId: Int, postedBy: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val job =
                    if (jobId != 0) {
                        database?.jobDao()?.getJobById(jobId, postedBy)
                    } else {
                        job()
                    }
                currentJob.postValue(job!!)
            }
        }
    }

    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = database?.userDao()?.getUserById(email)
                currentUser.postValue(user!!)
            }
        }
    }
}

