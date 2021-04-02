package com.csis4280.volunteerjobs.ui.authentication

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csis4280.volunteerjobs.ui.database.*
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

class AuthenticationViewModel(app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app)
    var currentUser = MutableLiveData<user>()
    var string: String = ""
    var mSocket: Socket? = null

    fun connectToServer(){
        try {
            mSocket = IO.socket("http://34.235.123.166:5000/")
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
        currentUser.value?.let {
            it.userName = username
            it.userEmail = userEmail
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    database?.userDao()?.insertUser(it)
                    val userName = username
                    val userEmail = userEmail
                    val jsonstring = "{'userName': '${userName}', 'userEmail': ${userEmail}}"
                    val jobj = JSONObject(jsonstring)
                    mSocket?.emit("addUser", jobj)
                }
            }
        }
    }




}