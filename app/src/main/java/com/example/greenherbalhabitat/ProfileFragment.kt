package com.example.greenherbalhabitat

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.greenherbalhabitat.databinding.FragmentProfileBinding
import com.example.greenherbalhabitat.viewmodels.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding :FragmentProfileBinding
    private val viewModel : UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        onBackButtonClicked()
        onLogOutClicked()
        return binding.root
    }

    private fun onLogOutClicked() {
        binding.llLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Log out")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.logOutUser()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
        }
    }


    private fun onBackButtonClicked() {
        binding.tbProfileFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
    }


}
