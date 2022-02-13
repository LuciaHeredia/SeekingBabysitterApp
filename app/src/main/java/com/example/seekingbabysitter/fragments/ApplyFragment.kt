package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class ApplyFragment : Fragment() {
    private var _binding: FragmentApplyBinding? = null
    private val binding get() = _binding!!

    private var validApply: Boolean = false
    private var validData: Boolean = false
    private var validAuth: Boolean = false
    private lateinit var database: DatabaseReference

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
                    binding.editTextPersonName.text.toString().trim { it <= ' ' },
                    binding.editTextPersonLastName.text.toString().trim { it <= ' ' },
                    binding.editTextAge.text.toString().toLong(),
                    binding.editTextPhone.text.toString().trim { it <= ' ' },
                    binding.editTextCity.text.toString().trim { it <= ' ' },
                    binding.editTextEmail.text.toString().trim { it <= ' ' },
                    binding.setUserId.text.toString().trim { it <= ' ' },
                    reviewed = false,
                    approved = false,
                    user_image = "Rtony11", // TODOO: change user_image
                    id_image = "R11" // TODOO: change id_image
                )

                // Firebase RealTime: Save Person's data
                database = Firebase.database.reference
                database.child(person.user_id.toString()).setValue(person).addOnSuccessListener {
                    Log.i("APPLY Firebase REALTIME:", "Success")

                    // Firebase Auth
                    val email = person.email.toString()
                    val password = binding.editTextPassword.text.toString().trim { it <= ' ' }
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("APPLY Firebase auth:", "Success")

                            val user = FirebaseAuth.getInstance().currentUser
                            user!!.sendEmailVerification()

                            // Firebase Storage
                            // TOOOO DOOOOOO


                            Snackbar.make(view,"Registration was successful! Verify your Email!",Snackbar.LENGTH_LONG).show()
                            Log.i("APPLY:", "Registration was successful!")
                            findNavController().navigate(R.id.action_applyFragment_to_homeFragment) // back Home


                        } else {
                            Snackbar.make(view,"Registration Failed!",Snackbar.LENGTH_LONG).show()
                            Log.i("APPLY Firebase auth:", "Failed")
                        }
                    }

                }.addOnFailureListener {
                    Snackbar.make(view,"Registration Failed!",Snackbar.LENGTH_LONG).show()
                    Log.i("APPLY Firebase REALTIME:", "Failed")
                }

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