package com.csis4280.volunteerjobs.ui.authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csis4280.volunteerjobs.ui.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AuthenticationViewModel(app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app)
    var currentUser = MutableLiveData<user>()
    fun addSampleData() {
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
               /* database?.userDao()?.insertUser(user("tobit", "toshum"))
                database?.jobDao()?.insertJob(job("cleaning", "Cleaning an old age home", "Cleaning", Date(), Date()))
                //database?.paeticipantDao()?.insertParticipant(participants(1,""))*/
            }
        }
    }

    fun getUserById(userEmail: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user =
                        if (userEmail != "") {
                            if(database?.userDao()?.getUserById(userEmail) !=null){
                                database.userDao()?.getUserById(userEmail)
                            }
                            else{
                                user()
                            }
                        } else {
                            user()
                        }
                currentUser.postValue(user!!)
            }
        }
    }

    fun setUser(){
        currentUser.postValue(user());
    }

    fun addUser(username: String, userEmail: String) {
        Log.i("Here", "Add1")
        currentUser.value?.let {
            it.userName = username
            it.userEmail = userEmail
            Log.i("Here", "Add2")
            viewModelScope.launch {
                Log.i("Here", "Add3")
                withContext(Dispatchers.IO) {
                    Log.i("Here", "Add4")
                    database?.userDao()?.insertUser(it)
                }
            }
        }
    }

}