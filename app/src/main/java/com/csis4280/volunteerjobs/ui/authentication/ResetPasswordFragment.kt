package com.csis4280.volunteerjobs.ui.authentication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.csis4280.volunteerjobs.R
import com.csis4280.volunteerjobs.databinding.FragmentResetPasswordBinding
import com.csis4280.volunteerjobs.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment() {

    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        binding = FragmentResetPasswordBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        binding.buttonSendResetEmail.setOnClickListener{
            sendEmailVerification()
        }

        return binding.root
    }

    private fun sendEmailVerification() {
        auth!!.sendPasswordResetEmail(binding.buttonSendResetEmail.text.toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT)
                } else {
                    Toast.makeText(context, "Please check the email you entered", Toast.LENGTH_SHORT)
                }
            }
    }
}