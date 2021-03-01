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
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.csis4280.volunteerjobs.MainActivity
import com.csis4280.volunteerjobs.databinding.ActivityAuthenticationBinding
import com.csis4280.volunteerjobs.databinding.FragmentLoginBinding
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.buttonLogin.setOnClickListener{
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
                            val user = auth.currentUser
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

                        }

                        // [END_EXCLUDE]
                    }
            }
            else{
                Toast.makeText(context, "Please enter valid username and password.", Toast.LENGTH_SHORT).show()
            }
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

}