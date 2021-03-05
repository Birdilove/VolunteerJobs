package com.csis4280.volunteerjobs.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csis4280.volunteerjobs.ui.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AuthenticationViewModel (app: Application) : AndroidViewModel(app){
    private val database = AppDatabase.getInstance(app)
     val List = database?.userDao()?.getAll()

    fun addSampleData() {
        viewModelScope.launch {

            withContext(Dispatchers.IO) {

                database?.userDao()?.insertNote(user("tobit","toshum"))
                database?.jobDao()?.insertJob(job("cleaning","Cleaning an old age home","Cleaning", Date(), Date()))
                database?.paeticipantDao()?.insertParticipant(participants(1,1))
            }
        }
    }
}