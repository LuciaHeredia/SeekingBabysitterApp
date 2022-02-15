package com.example.seekingbabysitter.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.databinding.FragmentUpdateBinding
import com.example.seekingbabysitter.model.Person
import com.example.seekingbabysitter.validator.Validator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateFragmentArgs by navArgs()

    private var validApply: Boolean = false
    private var validProfileImage: Boolean = false
    private var validIdImage: Boolean = false

    private var validInfoUpdate: Boolean = false
    private var validProfileImageUpdate: Boolean = false
    private var validIdImageUpdate: Boolean = false

    private var profileImageChanged: Boolean = false
    private var idImageChanged: Boolean = false

    private lateinit var database: DatabaseReference

    private lateinit var imageProUri: Uri
    private lateinit var imageIdUri: Uri
    private var typeImage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val view = binding.root

        val displayUser = args.babysitterUpdate // get data from UserFragment

        // convert jsonString to Character model
        val mapper = jacksonObjectMapper()
        val person: Person = mapper.readValue(displayUser, Person::class.java)

        loadUserDataFromDataBase(view, person)

        // Profile Image Select
        binding.buttonSelectImageProUpdate.setOnClickListener {
            selectProfileImage()
        }

        // Id Image Select
        binding.buttonSelectImageIdUpdate.setOnClickListener {
            selectIdImage()
        }

        // SAVE Button
        binding.buttonSaveUpdate.setOnClickListener {
            isUpdateValid()
            if(validApply &&
                 ((validProfileImage && profileImageChanged) || !profileImageChanged) &&
                        ((validIdImage && idImageChanged) || !idImageChanged) )  {
                updateNow(view, person)
            }else{
                Snackbar.make(view,R.string.check_images_info,Snackbar.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun loadUserDataFromDataBase(view: View, person: Person) {
        binding.editTextPersonNameUpdate.setText(person.first_name)
        binding.editTextPersonLastNameUpdate.setText(person.last_name)
        binding.editTextAgeUpdate.setText(person.age.toString())
        binding.editTextPhoneUpdate.setText(person.phone)
        binding.editTextCityUpdate.setText(person.city)
        binding.setEmailUpdate.text = person.email
        binding.setUserIdUpdate.text = person.user_id

        // Loading Alert-Dialog
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.setMessage("Loading...")
        val alert = dialogBuilder.create()
        alert.show()

        // retrieve and insert profile image of user from Firebase Storage
        val storageRefPro = FirebaseStorage.getInstance().reference.child("images/${person.profile_image}")
        val localFilePro = File.createTempFile("tempProfileImagePro","jpeg")
        storageRefPro.getFile(localFilePro).addOnSuccessListener {
            val bitmapPro = BitmapFactory.decodeFile(localFilePro.absolutePath)
            binding.proPicImageUpdate.background = null
            binding.proPicImageUpdate.setImageBitmap(bitmapPro)

            // retrieve and insert id image of user from Firebase Storage
            val storageRefId = FirebaseStorage.getInstance().reference.child("images/${person.id_image}")
            val localFileId = File.createTempFile("tempProfileImageId","jpeg")
            storageRefId.getFile(localFileId).addOnSuccessListener {
                val bitmapId = BitmapFactory.decodeFile(localFileId.absolutePath)
                binding.idPicImageUpdate.background = null
                binding.idPicImageUpdate.setImageBitmap(bitmapId)
                alert.dismiss() // stop showing loading-alert-dialog
            }.addOnFailureListener{
                alert.dismiss() // stop showing loading-alert-dialog
                Snackbar.make(view,"Failed to retrieve images!" , Snackbar.LENGTH_LONG).show()
                Log.i("Details User: Retrieve Profile Image:", "Failed")
            }

        }.addOnFailureListener{
            alert.dismiss() // stop showing loading-alert-dialog
            Snackbar.make(view,"Failed to retrieve images!" , Snackbar.LENGTH_LONG).show()
            Log.i("Details User: Retrieve Profile Image:", "Failed")
        }

        // Profile Image - approve - red alerts
        if(person.reviewed == false)
            binding.proTextApprove.setText(R.string.waiting_for_review)
        else if(person.reviewed == true && person.profile_approved == false)
            binding.proTextApprove.setText(R.string.change_image)
        else if(person.reviewed == true && person.profile_approved == true) {
            binding.proTextApprove.setText(R.string.image_approved)
            binding.proTextApprove.setTextColor(ContextCompat.getColor(activity!!,R.color.green))
        }

        // Id Image - approve - red alerts
        if(person.reviewed == false)
            binding.idTextApprove.setText(R.string.waiting_for_review)
        else if(person.reviewed == true && person.id_approved == false)
            binding.idTextApprove.setText(R.string.change_image)
        else if(person.reviewed == true && person.id_approved == true) {
            binding.idTextApprove.setText(R.string.image_approved)
            binding.idTextApprove.setTextColor(ContextCompat.getColor(activity!!,R.color.green))
        }

        // set approve(all) TEXT info
        if(person.approved == true) {
            binding.textApprove.setText(R.string.User_Data_Approved)
            binding.textApprove.setTextColor(ContextCompat.getColor(activity!!,R.color.green))
        }else if(person.profile_approved == true && person.id_approved == true)
            binding.textApprove.setText(R.string.check_info_user)
        else if(person.reviewed == false)
            binding.textApprove.setText(R.string.waiting_for_review)
        else
            binding.textApprove.setText(R.string.check_images_info)

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

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            if(typeImage == "profile_select") {
                imageProUri = data?.data!! // image selected, inside: imageProUri
                binding.proPicImageUpdate.setImageURI(imageProUri) // show image
                validProfileImage = true
                profileImageChanged = true
            }

            if(typeImage == "id_select") {
                imageIdUri = data?.data!! // image selected, inside: imageIdUri
                binding.idPicImageUpdate.setImageURI(imageIdUri) // show image
                validIdImage = true
                idImageChanged = true
            }
        }else {
            if (typeImage == "profile_select") {
                validProfileImage = false
            }

            if (typeImage == "id_select") {
                validIdImage = false
            }
        }

    }

    private fun isUpdateValid() {
        if(Validator.isValidName(binding.editTextPersonNameUpdate, true) &&
            Validator.isValidName(binding.editTextPersonLastNameUpdate, true) &&
            Validator.isValidAge(binding.editTextAgeUpdate, true) &&
            Validator.isValidPhone(binding.editTextPhoneUpdate, true) &&
            Validator.isValidName(binding.editTextCityUpdate, true) ) {
            validApply = true
        }
    }

    private fun updateNow(view: ScrollView, person: Person ) {

        // Uploading Alert-Dialog
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.setMessage("Uploading...")
        val alert = dialogBuilder.create()
        alert.show()

        /** Firebase Storage: START **/
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())

        // profile image upload
        val nowPro = Date()
        var fileNamePro = formatter.format(nowPro)

        /*** new profile_image uploaded ***/
        if(profileImageChanged){
            checkProfileImage(alert, fileNamePro, formatter, view, person)
        }else {
            fileNamePro = person.profile_image!!

            // id image upload
            val nowId = Date()
            var fileNameId = formatter.format(nowId)

            /*** new id_image uploaded ***/
            if(idImageChanged){
                checkIdImage(alert, fileNamePro, fileNameId, view, person)
            }else {
                fileNameId = person.id_image!!

                // USER to be saved in DB
                val newPerson = Person(
                    binding.editTextPersonNameUpdate.text.toString().trim { it <= ' ' },
                    binding.editTextPersonLastNameUpdate.text.toString().trim { it <= ' ' },
                    binding.editTextAgeUpdate.text.toString().toLong(),
                    binding.editTextPhoneUpdate.text.toString().trim { it <= ' ' },
                    binding.editTextCityUpdate.text.toString().trim { it <= ' ' },
                    binding.setEmailUpdate.text.toString().trim { it <= ' ' },
                    binding.setUserIdUpdate.text.toString().trim { it <= ' ' },
                    reviewed = false,
                    approved = false,
                    profile_image = fileNamePro,
                    id_image = fileNameId,
                    profile_approved = !profileImageChanged,
                    id_approved = !idImageChanged
                )

                /** Firebase RealTime: START **/
                // Save User's data
                database = Firebase.database.reference
                database.child(newPerson.user_id.toString()).setValue(newPerson).addOnSuccessListener {
                    Log.i("UPDATE Firebase REALTIME:", "Success")

                    /** SUCCESS **/
                    // continue if validation of all is true
                    alert.dismiss() // stop showing loading-alert-dialog
                    Snackbar.make(view,"UPDATE successful!",Snackbar.LENGTH_LONG).show()
                    Log.i("UPDATE:", "UPDATE successful!")

                    //convert Person model to jsonString
                    val mapperBackToUser = jacksonObjectMapper()
                    val jsonStringBackToUser = mapperBackToUser.writeValueAsString(newPerson)

                    //SafeArgs + Navigation
                    val directionsBackToUser = UpdateFragmentDirections.actionUpdateFragmentToUserFragment(jsonStringBackToUser)
                    findNavController().navigate(directionsBackToUser)


                }.addOnFailureListener {
                    alert.dismiss() // stop showing loading-alert-dialog
                    Snackbar.make(view,"UPDATE Failed! Check your information!",Snackbar.LENGTH_LONG).show()
                    Log.i("UPDATE Firebase REALTIME:", "Failed")
                }
                /** Firebase RealTime: END **/

            }
        }
        /** Firebase Storage: END **/

    }

    private fun checkProfileImage(alert: AlertDialog, fileNamePro: String, formatter: SimpleDateFormat, view: ScrollView, person: Person ) {
        // Delete from Firebase Storage the Old Profile Image that was saved
        val profileDelete = person.profile_image!!
        val storageReferenceProOld = FirebaseStorage.getInstance().getReference("images/$profileDelete")
        storageReferenceProOld.delete().addOnSuccessListener {
            Log.i("UPDATE Firebase Storage: Profile image deleted:", "Succeed")
        }.addOnFailureListener{
            Log.i("UPDATE Firebase Storage: Profile image deleted:", "Failed")
        }

        val storageReferencePro = FirebaseStorage.getInstance().getReference("images/$fileNamePro")
        storageReferencePro.putFile(imageProUri).addOnSuccessListener {
            binding.proPicImageUpdate.setImageURI(null)

            /*** new id_image uploaded ***/
            // id image upload
            val nowId = Date()
            var fileNameId = formatter.format(nowId)

            if(idImageChanged){
                checkIdImage(alert, fileNamePro, fileNameId, view, person)
            }else {
                fileNameId = person.id_image!!

                // USER to be saved in DB
                val newPerson = Person(
                    binding.editTextPersonNameUpdate.text.toString().trim { it <= ' ' },
                    binding.editTextPersonLastNameUpdate.text.toString().trim { it <= ' ' },
                    binding.editTextAgeUpdate.text.toString().toLong(),
                    binding.editTextPhoneUpdate.text.toString().trim { it <= ' ' },
                    binding.editTextCityUpdate.text.toString().trim { it <= ' ' },
                    binding.setEmailUpdate.text.toString().trim { it <= ' ' },
                    binding.setUserIdUpdate.text.toString().trim { it <= ' ' },
                    reviewed = false,
                    approved = false,
                    profile_image = fileNamePro,
                    id_image = fileNameId,
                    profile_approved = !profileImageChanged,
                    id_approved = !idImageChanged
                )

                /** Firebase RealTime: START **/
                // Save User's data
                database = Firebase.database.reference
                database.child(newPerson.user_id.toString()).setValue(newPerson).addOnSuccessListener {
                    Log.i("UPDATE Firebase REALTIME:", "Success")

                    /** SUCCESS **/
                    // continue if validation of all is true
                    alert.dismiss() // stop showing loading-alert-dialog
                    Snackbar.make(view,"UPDATE successful!",Snackbar.LENGTH_LONG).show()
                    Log.i("UPDATE:", "UPDATE successful!")

                    //convert Person model to jsonString
                    val mapperBackToUser = jacksonObjectMapper()
                    val jsonStringBackToUser = mapperBackToUser.writeValueAsString(newPerson)

                    //SafeArgs + Navigation
                    val directionsBackToUser = UpdateFragmentDirections.actionUpdateFragmentToUserFragment(jsonStringBackToUser)
                    findNavController().navigate(directionsBackToUser)


                }.addOnFailureListener {
                    alert.dismiss() // stop showing loading-alert-dialog
                    Snackbar.make(view,"UPDATE Failed! Check your information!",Snackbar.LENGTH_LONG).show()
                    Log.i("UPDATE Firebase REALTIME:", "Failed")
                }
                /** Firebase RealTime: END **/

            }

        }.addOnFailureListener{
            alert.dismiss() // stop showing loading-alert-dialog
            Snackbar.make(view,"Update Failed! Check Profile Image!",Snackbar.LENGTH_LONG).show()
            Log.i("UPDATE Firebase Storage: Profile image upload:", "Failed")
        }
    }

    private fun checkIdImage(alert: AlertDialog, fileNamePro: String, fileNameId: String, view: ScrollView, person: Person ) {
        // Delete from Firebase Storage the Old Id Image that was saved
        val idDelete = person.id_image!!
        val storageReferenceIdOld = FirebaseStorage.getInstance().getReference("images/$idDelete")
        storageReferenceIdOld.delete().addOnSuccessListener {
            Log.i("UPDATE Firebase Storage: Id image deleted:", "Succeed")
        }.addOnFailureListener{
            Log.i("UPDATE Firebase Storage: Id image deleted:", "Failed")
        }

        val storageReferenceId = FirebaseStorage.getInstance().getReference("images/$fileNameId")
        storageReferenceId.putFile(imageIdUri).addOnSuccessListener {
            binding.idPicImageUpdate.setImageURI(null)

            // USER to be saved in DB
            val newPerson = Person(
                binding.editTextPersonNameUpdate.text.toString().trim { it <= ' ' },
                binding.editTextPersonLastNameUpdate.text.toString().trim { it <= ' ' },
                binding.editTextAgeUpdate.text.toString().toLong(),
                binding.editTextPhoneUpdate.text.toString().trim { it <= ' ' },
                binding.editTextCityUpdate.text.toString().trim { it <= ' ' },
                binding.setEmailUpdate.text.toString().trim { it <= ' ' },
                binding.setUserIdUpdate.text.toString().trim { it <= ' ' },
                reviewed = false,
                approved = false,
                profile_image = fileNamePro,
                id_image = fileNameId,
                profile_approved = !profileImageChanged,
                id_approved = !idImageChanged
            )

            /** Firebase RealTime: START **/
            // Save User's data
            database = Firebase.database.reference
            database.child(newPerson.user_id.toString()).setValue(newPerson).addOnSuccessListener {
                Log.i("UPDATE Firebase REALTIME:", "Success")

                /** SUCCESS **/
                // continue if validation of all is true
                alert.dismiss() // stop showing loading-alert-dialog
                Snackbar.make(view,"UPDATE successful!",Snackbar.LENGTH_LONG).show()
                Log.i("UPDATE:", "UPDATE successful!")

                //convert Person model to jsonString
                val mapperBackToUser = jacksonObjectMapper()
                val jsonStringBackToUser = mapperBackToUser.writeValueAsString(newPerson)

                //SafeArgs + Navigation
                val directionsBackToUser = UpdateFragmentDirections.actionUpdateFragmentToUserFragment(jsonStringBackToUser)
                findNavController().navigate(directionsBackToUser)


            }.addOnFailureListener {
                alert.dismiss() // stop showing loading-alert-dialog
                Snackbar.make(view,"UPDATE Failed! Check your information!",Snackbar.LENGTH_LONG).show()
                Log.i("UPDATE Firebase REALTIME:", "Failed")
            }
            /** Firebase RealTime: END **/

        }.addOnFailureListener{
            alert.dismiss() // stop showing loading-alert-dialog
            Snackbar.make(view,"UPDATE Failed! Check Id Image!",Snackbar.LENGTH_LONG).show()
            Log.i("UPDATE Firebase Storage: Id image upload:", "Failed")
        }
    }

}