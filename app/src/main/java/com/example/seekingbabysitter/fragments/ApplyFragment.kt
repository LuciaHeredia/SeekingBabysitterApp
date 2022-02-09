package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seekingbabysitter.validator.Validator
import com.example.seekingbabysitter.databinding.FragmentApplyBinding

class ApplyFragment : Fragment() {
    private var _binding: FragmentApplyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApplyBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonApply.setOnClickListener { signUp() } // Validation

        return view
    }

    private fun signUp() {
        Validator.isValidName(binding.editTextPersonName, true)
        Validator.isValidName(binding.editTextPersonLastName, true)
        Validator.isValidAge(binding.editTextAge, true)
        Validator.isValidPhone(binding.editTextPhone, true)
        Validator.isValidName(binding.editTextCity, true)
        Validator.isValidEmail(binding.editTextEmail, true)
        Validator.isValidPassword(binding.editTextPassword, true)
        // profile pic - must be uploaded
        // id pic - must be uploaded
    }

}