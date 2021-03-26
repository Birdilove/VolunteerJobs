package com.csis4280.volunteerjobs.ui.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.csis4280.volunteerjobs.MainActivity
import com.csis4280.volunteerjobs.databinding.FragmentLoginBinding
import com.csis4280.volunteerjobs.ui.database.AppDatabase
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        binding.buttonLogin.setOnClickListener{
            viewModel.setUser()
            login()
        }

        binding.textViewSignUp.setOnClickListener{
            HapticFeedbackConstants.VIRTUAL_KEY
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }

        binding.textViewResetPassword.setOnClickListener{
            HapticFeedbackConstants.VIRTUAL_KEY
            val action = LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
            findNavController().navigate(action)
        }
       return binding.root
    }

    private fun login(){
        if(binding.editTextTextEmailAddress.text.toString().trim() != "" && binding.editTextTextPassword.text.toString().trim() != "") {
            HapticFeedbackConstants.VIRTUAL_KEY
            auth.signInWithEmailAndPassword(
                binding.editTextTextEmailAddress.text.toString(),
                binding.editTextTextPassword.text.toString()
            )
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        viewModel.getUserById(auth.currentUser?.email.toString())
                        viewModel.addUser(auth.currentUser?.email.toString(),auth.currentUser?.email.toString())
                        Toast.makeText(
                            context, "Authentication Success.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            context, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // [START_EXCLUDE]
                    if (!task.isSuccessful) {
                        Toast.makeText(context, "Please enter valid username and password.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else{
            Toast.makeText(context, "Please enter valid username and password.", Toast.LENGTH_SHORT).show()
        }
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }



}