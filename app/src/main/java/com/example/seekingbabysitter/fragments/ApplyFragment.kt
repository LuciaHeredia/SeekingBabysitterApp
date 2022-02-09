package com.example.seekingbabysitter.fragments

import android.os.Bundle
import com.example.seekingbabysitter.model.Person
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.validator.Validator
import com.example.seekingbabysitter.databinding.FragmentApplyBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class ApplyFragment : Fragment() {
    private var _binding: FragmentApplyBinding? = null
    private val binding get() = _binding!!

    private var validApply: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApplyBinding.inflate(inflater, container, false)
        val view = binding.root

        setUserId() // setting random userId

        binding.buttonApply.setOnClickListener {
            signUp()
            if(validApply) {
                val person = Person(
                    binding.editTextPersonName.toString(),
                    binding.editTextPersonLastName.toString(),
                    binding.editTextAge.toString().toInt(),
                    binding.editTextPhone.toString(),
                    binding.editTextCity.toString(),
                    binding.editTextEmail.toString(),
                    binding.setUserId.toString(),
                    binding.editTextPassword.toString(),
                    reviewed = false,
                    approved = false,
                    user_image = R.drawable.tony_11, // TODOO: change user_image
                    id_image = R.drawable.tony_11 // TODOO: change id_image
                )
                    // TODOO: firebase - save auth
                        // TODOO: firebase - save details
                            // TODOO: firebase - save pictures
                Snackbar.make(binding.applyLayout, "Registration was successful!", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_applyFragment_to_homeFragment) // back Home
            }
        }

        return view
    }

    private fun setUserId() {
        // TODOO: user id - if already in firebase, change to another random one
        binding.setUserId.text = UUID.randomUUID().toString().substring(0,8)
    }

    private fun signUp() {
        if(Validator.isValidName(binding.editTextPersonName, true) &&
        Validator.isValidName(binding.editTextPersonLastName, true) &&
        Validator.isValidAge(binding.editTextAge, true) &&
        Validator.isValidPhone(binding.editTextPhone, true) &&
        Validator.isValidName(binding.editTextCity, true) &&
        Validator.isValidEmail(binding.editTextEmail, true) &&
        Validator.isValidPassword(binding.editTextPassword, true) ) {
            validApply = true
        }

        // TODOO: profile pic - must be uploaded
        // TODOO: id pic - must be uploaded
    }

}