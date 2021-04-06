package com.csis4280.volunteerjobs.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.csis4280.volunteerjobs.R
import com.csis4280.volunteerjobs.databinding.FragmentLogOutBinding
import com.csis4280.volunteerjobs.databinding.FragmentPostJobBinding
import com.google.firebase.auth.FirebaseAuth

class LogOutFragment : Fragment() {

    private lateinit var binding: FragmentLogOutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLogOutBinding.inflate(inflater, container, false)

        binding.buttonLogOut.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setIcon(R.drawable.ic_baseline_exit_to_app_24)

            builder.setPositiveButton("Yes") { dialogInterface, which ->
                FirebaseAuth.getInstance().signOut();
                requireActivity().finish()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        return binding.root
    }

}