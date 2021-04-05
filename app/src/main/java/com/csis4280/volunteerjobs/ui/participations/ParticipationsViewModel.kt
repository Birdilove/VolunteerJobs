package com.csis4280.volunteerjobs.ui.participations

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.csis4280.volunteerjobs.loggedInUser
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.database.participants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ParticipationsViewModel(app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app)
    val jobList = database?.jobDao()?.getAll()
    val signedUpjobList = database?.paeticipantDao()?.getAllParticipations(loggedInUser)

    fun updateParticipantsList(participants: List<participants>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.paeticipantDao()?.insertAllParticipants(participants)
            }
        }
    }

    fun deleteParticipation(participants: List<participants>){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.paeticipantDao()?.deleteParticipation(participants)
            }
        }
    }
}