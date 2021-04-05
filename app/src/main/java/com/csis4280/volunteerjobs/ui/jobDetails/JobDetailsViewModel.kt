package com.csis4280.volunteerjobs.ui.jobDetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csis4280.volunteerjobs.loggedInUser
import com.csis4280.volunteerjobs.mSocket
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.database.participants
import com.csis4280.volunteerjobs.ui.database.user
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class JobDetailsViewModel(app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app)
    val signedUpjobList = database?.paeticipantDao()
        ?.getAllParticipations(loggedInUser)
    val currentJob = MutableLiveData<job>()
    val currentUser = MutableLiveData<user>()
    val currentParticipant = MutableLiveData<participants>()

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

    fun updateJob() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentJob.value?.let {
                    database?.jobDao()?.insertJob(it)

                    val jobId = it.jobId
                    val jobTitle = it.jobTitle
                    val jobType = it.jobType
                    val jobDesc = it.jobDescription
                    val jobStartDate = it.startDate
                    val jobEndDate = it.endDate
                    val postedBy = it.postedBy
                    val jsonstring: String =
                        "{'jobId': ${jobId}, 'jobTitle': '${jobTitle}', 'jobType': '${jobType}', 'jobDesc': '${jobDesc}', 'jobStartDate': '${jobStartDate}', 'jobEndDate': '${jobEndDate}', 'postedBy': '${postedBy}'}"
                    val jobj = JSONObject(jsonstring)
                    mSocket?.emit("updateJob", jobj)
                }
            }
        }
    }

    fun deleteJob() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentJob.value?.let { database?.jobDao()?.deleteJob(it)
                    val jobId = it.jobId
                    val postedBy = it.postedBy
                    val jsonString: String =
                        "{'jobId': ${jobId}, 'postedBy': '${postedBy}'}"
                    val jobj = JSONObject(jsonString)
                    mSocket?.emit("deleteJob", jobj)
                }
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

    fun signOutOfJob(jobId: Int, postedBy: String, userEmail: String) {
        currentParticipant.value?.let {
            it.jobId = jobId
            it.postedBy = postedBy
            it.userEmail = userEmail
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    database?.paeticipantDao()?.deleteEntry(it)
                    val jobId = it.jobId
                    val postedBy = it.postedBy
                    val userEmail = it.userEmail
                    val jsonString: String =
                        "{'jobId': ${jobId}, 'postedBy': '${postedBy}', 'userEmail': '${userEmail}'}"
                    val jobj = JSONObject(jsonString)
                    mSocket?.emit("deleteParticipation", jobj)
                }
            }
        }
    }

    fun addToParticipation(jobId: Int, postedBy: String, userEmail: String) {
        currentParticipant.value?.let {
            it.jobId = jobId
            it.postedBy = postedBy
            it.userEmail = userEmail
            Log.i("Participation Data" , "USER ID "+ it.jobId + " PostedBy "+ it.postedBy +" Email "+ it.userEmail+" Log "+ loggedInUser)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    database?.paeticipantDao()?.insertParticipant(it)
                    val jobId = it.jobId
                    val postedBy = it.postedBy
                    val userEmail = it.userEmail
                    val jsonString: String =
                        "{'jobId': ${jobId}, 'postedBy': '${postedBy}', 'userEmail': '${userEmail}'}"
                    val jobj = JSONObject(jsonString)
                    mSocket?.emit("addToParticipation", jobj)
                }
            }
        }
    }
}