package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.databinding.FragmentLoginBinding
import com.example.seekingbabysitter.validator.Validator

class LogInFragment  : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        loginValidation()

        return view
    }

    private fun loginValidation() {
        binding.buttonEnter.setOnClickListener {

            if (Validator.isValidSection(binding.editTextUserIdLogin, true) &&
                Validator.isValidSection(binding.editTextPasswordLogin, true)) {
                // TODOO: firebase - check if verified email
                // TODOO: firebase - check if auth successfully
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

}