package com.example.seekingbabysitter.fragments

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.databinding.FragmentLoginBinding
import com.example.seekingbabysitter.model.Person
import com.example.seekingbabysitter.validator.Validator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LogInFragment  : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.forgotPasswordText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        // FORGOT PASSWORD
        binding.forgotPasswordText.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_ForgotPassFragment)
        }

        loginValidation(view)

        return view
    }

    private fun loginValidation(view: View) {
        binding.buttonEnter.setOnClickListener {

            // loading Alert-Dialog
            val dialogBuilder = AlertDialog.Builder(activity!!)
            dialogBuilder.setMessage("Loading...")
            val alert = dialogBuilder.create()
            alert.show()

            if (Validator.isValidSection(binding.editTextUserIdLogin, true) &&
                Validator.isValidSection(binding.editTextPasswordLogin, true)) {
                    val userId: String = binding.editTextUserIdLogin.text.toString().trim { it <= ' ' }
                    val password: String = binding.editTextPasswordLogin.text.toString().trim { it <= ' ' }

                    /** Firebase RealTime: START **/
                    // get email from Firebase Realtime
                    database = FirebaseDatabase.getInstance().reference
                    database.child(userId).get().addOnSuccessListener {
                        if(it.exists()) {
                            val person = Person(
                                it.child("first_name").value.toString(),
                                it.child("last_name").value.toString(),
                                "fff", //TEST
                                it.child("age").value as Long?,
                                it.child("phone").value.toString(),
                                it.child("city").value.toString(),
                                it.child("email").value.toString(),
                                it.child("user_id").value.toString(),
                                it.child("reviewed").value as Boolean?,
                                it.child("approved").value as Boolean?,
                                it.child("profile_image").value.toString(),
                                it.child("id_image").value.toString(),
                                it.child("profile_approved").value as Boolean?,
                                it.child("id_approved").value as Boolean?
                            )

                            /** Firebase Auth: START **/
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(person.email.toString(), password).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = FirebaseAuth.getInstance().currentUser
                                    val emailVerified = user!!.isEmailVerified

                                    if(emailVerified) {
                                        Log.i("APPLY Firebase auth:", "Success")
                                        Snackbar.make(view,"LOGIN Succeed!",Snackbar.LENGTH_LONG).show()
                                        //convert Person model to jsonString
                                        val mapper = jacksonObjectMapper()
                                        val jsonString = mapper.writeValueAsString(person)

                                        /** SUCCESS **/
                                        //SafeArgs + Navigation
                                        alert.dismiss() // stop showing loading-alert-dialog
                                        val directions = LogInFragmentDirections.actionLoginFragmentToUserFragment(jsonString)
                                        findNavController().navigate(directions)
                                    }
                                    else {
                                        alert.dismiss() // stop showing loading-alert-dialog
                                        Snackbar.make(view,"Verify your Email first!", Snackbar.LENGTH_LONG).show()
                                    }

                                }
                                else {
                                    alert.dismiss() // stop showing loading-alert-dialog
                                    Snackbar.make(view,"LOGIN Failed!", Snackbar.LENGTH_LONG).show()
                                    Log.i("APPLY Firebase auth:", "Failed")
                                }
                            }
                            /** Firebase Auth: END **/

                        }else {
                            alert.dismiss() // stop showing loading-alert-dialog
                            Snackbar.make(view,"User doesn't exit!",Snackbar.LENGTH_LONG).show()
                            Log.i("LOGIN:", "User doesn't exit!")
                        }

                    }.addOnFailureListener {
                        alert.dismiss() // stop showing loading-alert-dialog
                        Snackbar.make(view,"LOGIN Failed!",Snackbar.LENGTH_LONG).show()
                        Log.i("LOGIN:", "Failed!")
                    }
                /** Firebase RealTime: END **/
            }
        }
    }
}