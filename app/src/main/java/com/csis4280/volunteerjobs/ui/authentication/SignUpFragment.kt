package com.csis4280.volunteerjobs.ui.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.csis4280.volunteerjobs.MainActivity
import com.csis4280.volunteerjobs.R
import com.csis4280.volunteerjobs.databinding.FragmentLoginBinding
import com.csis4280.volunteerjobs.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        binding.buttonSignUp.setOnClickListener{
            createAccount(binding.editTextSignUpEmail.text.toString().trim(), binding.editTextSignUpPassword.text.toString().trim() )
        }

        binding.textViewAlreadyUser.setOnClickListener{
            HapticFeedbackConstants.VIRTUAL_KEY
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}