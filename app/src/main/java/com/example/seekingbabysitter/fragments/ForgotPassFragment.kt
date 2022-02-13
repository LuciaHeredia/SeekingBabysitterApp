package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.databinding.FragmentForgotPassBinding
import com.example.seekingbabysitter.validator.Validator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPassFragment : Fragment() {
    private var _binding: FragmentForgotPassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForgotPassBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonSendReset.setOnClickListener {

            if(Validator.isValidSection(binding.editTextEmailReset, true)) {
                val email: String = binding.editTextEmailReset.text.toString().trim{ it <= ' '}
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{task ->
                    if(task.isSuccessful) {
                        Snackbar.make(view, "Email sent successfully to reset your password!", Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_ForgotPassFragment_to_loginFragment)
                    }else{
                        Snackbar.make(view, task.exception!!.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }
}