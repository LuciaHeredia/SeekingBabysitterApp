package com.example.seekingbabysitter.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.seekingbabysitter.model.Person
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
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
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class ApplyFragment : Fragment() {
    private var _binding: FragmentApplyBinding? = null
    private val binding get() = _binding!!

    private var validApply: Boolean = false
    private var validProfileImage: Boolean = false
    private var validIdImage: Boolean = false

    private lateinit var database: DatabaseReference

    private lateinit var imageProUri: Uri
    private lateinit var imageIdUri: Uri
    private var typeImage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApplyBinding.inflate(inflater, container, false)
        val view = binding.root

        setUserId() // setting random userId

        // Profile Image Select
        binding.buttonSelectImagePro.setOnClickListener {
            selectProfileImage()
        }

        // Id Image Select
        binding.buttonSelectImageId.setOnClickListener {
            selectIdImage()
        }

        // Apply Button
        binding.buttonApply.setOnClickListener {
            isSignUpValid()
            if(validApply && validProfileImage && validIdImage) {
                signUp(view)
            }else{
                Snackbar.make(view,"Check Images!",Snackbar.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun setUserId() {
        // TODOO: user id - if already in firebase, change to another random one
        binding.setUserId.text = UUID.randomUUID().toString().substring(0,8)
    }

    private fun selectProfileImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        typeImage = "profile_select"
        startActivityForResult(intent, 100)
    }

    private fun selectIdImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        typeImage = "id_select"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            if(typeImage == "profile_select") {
                imageProUri = data?.data!! // image selected, inside: imageProUri
                binding.proPicImage.setImageURI(imageProUri) // show image
                validProfileImage = true
            }

            if(typeImage == "id_select") {
                imageIdUri = data?.data!! // image selected, inside: imageIdUri
                binding.idPicImage.setImageURI(imageIdUri) // show image
                validIdImage = true
            }
        }else {
            if (typeImage == "profile_select")
                validProfileImage = false

            if (typeImage == "id_select")
                validIdImage = false
        }

    }

    private fun isSignUpValid() {
        if(Validator.isValidName(binding.editTextPersonName, true) &&
        Validator.isValidName(binding.editTextPersonLastName, true) &&
        Validator.isValidAge(binding.editTextAge, true) &&
        Validator.isValidPhone(binding.editTextPhone, true) &&
        Validator.isValidName(binding.editTextCity, true) &&
        Validator.isValidEmail(binding.editTextEmail, true) &&
        Validator.isValidPassword(binding.editTextPassword, true) ) {
            validApply = true
        }
    }

    private fun signUp(view: ScrollView) {

        // Loading Alert-Dialog
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.setMessage("Uploading...")
        val alert = dialogBuilder.create()
        alert.show()

        /** Firebase Storage: START **/
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())

        // profile image upload
        val nowPro = Date()
        val fileNamePro = formatter.format(nowPro)
        val storageReferencePro = FirebaseStorage.getInstance().getReference("images/$fileNamePro")
        storageReferencePro.putFile(imageProUri).addOnSuccessListener {
            binding.proPicImage.setImageURI(null)

            // id image upload
            val nowId = Date()
            val fileNameId = formatter.format(nowId)
            val storageReferenceId = FirebaseStorage.getInstance().getReference("images/$fileNameId")
            storageReferenceId.putFile(imageIdUri).addOnSuccessListener {
                binding.idPicImage.setImageURI(null)

                // USER to be saved in DB
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
                    user_image = fileNamePro,
                    id_image = fileNameId
                )

                /** Firebase RealTime: START **/
                // Save User's data
                database = Firebase.database.reference
                database.child(person.user_id.toString()).setValue(person).addOnSuccessListener {
                    Log.i("APPLY Firebase REALTIME:", "Success")

                    /** Firebase Auth: START **/
                    val email = person.email.toString()
                    val password = binding.editTextPassword.text.toString().trim { it <= ' ' }
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("APPLY Firebase auth:", "Success")

                            val user = FirebaseAuth.getInstance().currentUser
                            user!!.sendEmailVerification()

                            /** SUCCESS **/
                            alert.dismiss() // stop showing loading-alert-dialog
                            Snackbar.make(view,"Registration was successful! We sent you a verification email!",Snackbar.LENGTH_LONG).show()
                            Log.i("APPLY:", "Registration was successful!")
                            findNavController().navigate(R.id.action_applyFragment_to_homeFragment) // back Home

                        } else {
                            alert.dismiss() // stop showing loading-alert-dialog
                            Snackbar.make(view,"Registration Failed! Check Email section!",Snackbar.LENGTH_LONG).show()
                            Log.i("APPLY Firebase auth:", "Failed")

                            // Delete from Firebase Storage the Profile Image that was saved
                            storageReferencePro.delete().addOnSuccessListener {
                                Log.i("APPLY Firebase Storage: Profile image deleted:", "Succeed")
                            }.addOnFailureListener{
                                Log.i("APPLY Firebase Storage: Profile image deleted:", "Failed")
                            }

                            // Delete from Firebase Storage the Id Image that was saved
                            storageReferenceId.delete().addOnSuccessListener {
                                Log.i("APPLY Firebase Storage: Id image deleted:", "Succeed")
                            }.addOnFailureListener{
                                Log.i("APPLY Firebase Storage: Id image deleted:", "Failed")
                            }

                            // Delete from Firebase REALTIME the user id that was saved
                            database.child(person.user_id.toString()).removeValue()

                        }
                    }
                    /** Firebase Auth: END **/

                }.addOnFailureListener {
                    alert.dismiss() // stop showing loading-alert-dialog
                    Snackbar.make(view,"Registration Failed! Check your information!",Snackbar.LENGTH_LONG).show()
                    Log.i("APPLY Firebase REALTIME:", "Failed")

                    // Delete from Firebase Storage the Profile Image that was saved
                    storageReferencePro.delete().addOnSuccessListener {
                        Log.i("APPLY Firebase Storage: Profile image deleted:", "Succeed")
                    }.addOnFailureListener{
                        Log.i("APPLY Firebase Storage: Profile image deleted:", "Failed")
                    }

                    // Delete from Firebase Storage the Id Image that was saved
                    storageReferenceId.delete().addOnSuccessListener {
                        Log.i("APPLY Firebase Storage: Id image deleted:", "Succeed")
                    }.addOnFailureListener{
                        Log.i("APPLY Firebase Storage: Id image deleted:", "Failed")
                    }

                }
                /** Firebase RealTime: END **/


            }.addOnFailureListener{
                alert.dismiss() // stop showing loading-alert-dialog
                Snackbar.make(view,"Registration Failed! Check Id Image!",Snackbar.LENGTH_LONG).show()
                Log.i("APPLY Firebase Storage: Id image upload:", "Failed")

                // Delete from Firebase Storage the Profile Image that was saved
                storageReferencePro.delete().addOnSuccessListener {
                    Log.i("APPLY Firebase Storage: Profile image deleted:", "Succeed")
                }.addOnFailureListener{
                    Log.i("APPLY Firebase Storage: Profile image deleted:", "Failed")
                }
            }

        }.addOnFailureListener{
            alert.dismiss() // stop showing loading-alert-dialog
            Snackbar.make(view,"Registration Failed! Check Profile Image!",Snackbar.LENGTH_LONG).show()
            Log.i("APPLY Firebase Storage: Profile image upload:", "Failed")
        }
        /** Firebase Storage: END **/

    }

}