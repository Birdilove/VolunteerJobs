package com.csis4280.volunteerjobs.ui.postJob

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csis4280.volunteerjobs.URL
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.csis4280.volunteerjobs.ui.database.job
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URISyntaxException

class PostJobViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val currentJob = MutableLiveData<job>()
    var string: String = ""
    var mSocket: Socket? = null
    var maxId: Int = 0

    fun updateJobList(jobList: List<job>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.i("ADDDATA", "HERE")
                database?.jobDao()?.insertAllJob(jobList)
            }
        }
    }

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

    fun getMaxId(email: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (database?.jobDao()?.getMAXJobId(email) != null) {
                    maxId = database.jobDao()?.getMAXJobId(email)!!
                }
            }
        }
    }

    fun updateJob() {
        currentJob.value?.let {
            it.jobId = maxId + 1
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
                        database?.jobDao()?.insertJob(it)

                        try {
                            mSocket = IO.socket(URL)
                        } catch (e: URISyntaxException) {
                            Log.d("URI error", e.message.toString())
                        }

                        try {
                            mSocket?.connect()
                            Log.i("Connection ", string)
                        } catch (e: Exception) {
                            string + " Failed to connect. " + e.message
                            Log.i("Connection ", string)
                        }

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
                        mSocket?.emit("newJob", jobj)
                    }
                }
            }
        }
    }
}
