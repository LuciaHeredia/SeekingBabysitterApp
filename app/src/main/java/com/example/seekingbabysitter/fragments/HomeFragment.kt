package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser




class HomeFragment : Fragment(){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // logout
        FirebaseAuth.getInstance().signOut()

        binding.applyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_applyFragment)
        }

        binding.listBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_listFragment)
        }

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        return view
    }

}