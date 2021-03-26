package com.csis4280.volunteerjobs.ui.participations

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.database.participants
import com.google.firebase.auth.FirebaseAuth

class ParticipationsViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val jobList = database?.paeticipantDao()?.getAllParticipations(FirebaseAuth.getInstance().currentUser?.email.toString())
    val signedUpjobList = database?.paeticipantDao()?.getAllParticipations(FirebaseAuth.getInstance().currentUser?.email.toString())


}