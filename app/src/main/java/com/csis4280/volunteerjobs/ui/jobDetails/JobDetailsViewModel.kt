package com.csis4280.volunteerjobs.ui.jobDetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.database.participants
import com.csis4280.volunteerjobs.ui.database.user
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobDetailsViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val signedUpjobList = database?.paeticipantDao()
        ?.getAllParticipations(FirebaseAuth.getInstance().currentUser?.email.toString())
    val currentJob = MutableLiveData<job>()
    val currentUser = MutableLiveData<user>()
    val currentParticipant = MutableLiveData<participants>()

    fun getJobById(jobId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val job =
                    if (jobId != 0) {
                        database?.jobDao()?.getJobById(jobId)
                    } else {
                        job()
                    }
                currentJob.postValue(job!!)
            }
        }
    }

    fun updateJob() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentJob.value?.let { database?.jobDao()?.insertJob(it) }
            }
        }
    }

    fun deleteJob() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentJob.value?.let { database?.jobDao()?.deleteJob(it) }
            }
        }
    }

    fun getParticipantById(id: Int, userEmail: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val participation =
                    if (id != 0) {
                        if (database?.paeticipantDao()?.getParticipentById(id, userEmail) != null) {
                            Log.i("DAO", "HERE")
                            database.paeticipantDao()?.getParticipentById(id, userEmail)
                        } else {
                            participants()
                        }
                    } else {
                        participants()
                    }
                currentParticipant.postValue(participation!!)
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

    fun signOutOfJob() {
        currentParticipant.value.let {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it != null) {
                        database?.paeticipantDao()?.deleteEntry(it)
                    }
                }
            }
        }
    }

    fun addToParticipation(jobId: Int, userId: Int, userEmail: String) {
        currentParticipant.value?.let {
            it.jobId = jobId
            it.userId = userId
            it.userEmail = userEmail
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    database?.paeticipantDao()?.insertParticipant(it)
                }
            }
        }
    }
}