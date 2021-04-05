package com.csis4280.volunteerjobs

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.database.participants
import com.csis4280.volunteerjobs.ui.participations.ParticipationsViewModel
import com.csis4280.volunteerjobs.ui.postJob.PostJobViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

var mSocket: Socket? = null
var string: String = ""
const val URL = "http://52.55.26.25:5000/"
var loggedInUser = FirebaseAuth.getInstance().currentUser?.email.toString()

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val typeJob = Types.newParameterizedType(List::class.java, job::class.java)
    private val typeParticipations =
        Types.newParameterizedType(List::class.java, participants::class.java)
    private lateinit var postJobViewModel: PostJobViewModel
    private lateinit var participationsViewModel: ParticipationsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val navController = findNavController(R.id.fragment2)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_post, R.id.navigation_participations
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        postJobViewModel = ViewModelProvider(this).get(PostJobViewModel::class.java)
        participationsViewModel = ViewModelProvider(this).get(ParticipationsViewModel::class.java)

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
        mSocket?.emit("getData")
        mSocket?.emit("getParticipants")
        mSocket?.on("notification", onNewMessage)
        mSocket?.on("deleteJob", onDeleteJob)
        mSocket?.on("datasent", onDataUpdateJobList)
        mSocket?.on("updateJob", onUpdateJob)
        mSocket?.on("updateParticipants", onDataParticipantsUpdate)
        mSocket?.on("deleteParticipants", onDeleteParticipation)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
    }

    private val onDataUpdateJobList = Emitter.Listener { args ->
        this.runOnUiThread {
            val data = args[0] as String

            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter: JsonAdapter<List<job>> = moshi.adapter(typeJob)

            val dataList = adapter.fromJson(data)
            if (dataList != null) {
                postJobViewModel.updateJobList(dataList)
            }
        }
    }

    private val onDataParticipantsUpdate = Emitter.Listener { args ->
        this.runOnUiThread {
            val data = args[0] as String
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter: JsonAdapter<List<participants>> = moshi.adapter(typeParticipations)
            val dataList = adapter.fromJson(data)

            if (dataList != null) {
                participationsViewModel.updateParticipantsList(dataList)
            }
        }
    }

    private val onNewMessage =
        Emitter.Listener { args ->
            this.runOnUiThread(Runnable {
                val data = args[0] as String
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
            })
        }

    private val onDeleteJob =
        Emitter.Listener { args ->
            this.runOnUiThread {
                val data = args[0] as String
                val moshi: Moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val adapter: JsonAdapter<List<job>> = moshi.adapter(typeJob)
                val dataList = adapter.fromJson(data)
                if (dataList != null) {
                    postJobViewModel.deleteJob(dataList)
                }
            }
        }

    private val onUpdateJob =
        Emitter.Listener { args ->
            this.runOnUiThread {
                val data = args[0] as String
                val moshi: Moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                Log.i("Datasent", "HERE")
                val adapter: JsonAdapter<List<job>> = moshi.adapter(typeJob)
                val dataList = adapter.fromJson(data)
                if (dataList != null) {
                    postJobViewModel.updateJobList(dataList)
                }
            }
        }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)
        (fragment as? IOnBackPressed)?.onBackPressed()?.let {
            super.onBackPressed()
        }
    }

    private val onDeleteParticipation = Emitter.Listener { args ->
        this.runOnUiThread {
            val data = args[0] as String
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter: JsonAdapter<List<participants>> = moshi.adapter(typeParticipations)
            Log.i("DELETE", "DELETE")
            val dataList = adapter.fromJson(data)

            if (dataList != null) {
                participationsViewModel.deleteParticipation(dataList)
            }
        }
    }
}

interface IOnBackPressed {
    fun onBackPressed()
}