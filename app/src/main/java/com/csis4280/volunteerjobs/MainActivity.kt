package com.csis4280.volunteerjobs

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.csis4280.volunteerjobs.ui.database.job
import com.csis4280.volunteerjobs.ui.postJob.PostJobViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URISyntaxException

var string: String = ""
const val URL = "http://3.89.150.41:5000/"
class MainActivity : AppCompatActivity() {
    var mSocket: Socket? = null
    private lateinit var auth: FirebaseAuth
    private val myType = Types.newParameterizedType(List::class.java, job::class.java)
    private lateinit var postJobViewModel: PostJobViewModel
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
        mSocket?.on("notification", onNewMessage)
        mSocket?.on("datasent", onDataRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
    }

    private val onDataRequest = Emitter.Listener { args ->
        this.runOnUiThread {
            val data = args[0] as String

            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter : JsonAdapter<List<job>> = moshi.adapter(myType)

            val dataList = adapter.fromJson(data)

            if (dataList != null) {
                postJobViewModel.updateJobList(dataList)
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
}