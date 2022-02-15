package com.example.seekingbabysitter.fragments

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.seekingbabysitter.databinding.FragmentReviewDetailsBinding
import com.example.seekingbabysitter.model.Person
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ReviewDetailsFragment : Fragment() {
    private var _binding: FragmentReviewDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    private val args: ReviewDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough() // MaterialDesign Transition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReviewDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val displayUser = args.babysitterReview // get data from ReviewListFragment

        // convert jsonString to Character model
        val mapper = jacksonObjectMapper()
        val person: Person = mapper.readValue(displayUser, Person::class.java)

        loadUserDataFromDataBase(view, person)

        // Profile Image needs to be changed
        binding.btnChangeProfile.setOnClickListener {
            Snackbar.make(view,"saved",Snackbar.LENGTH_SHORT).show()
            person.profile_approved = false
        }

        // Profile Image OK
        binding.btnOkProfile.setOnClickListener {
            Snackbar.make(view,"saved",Snackbar.LENGTH_SHORT).show()
            person.profile_approved = true
        }

        // Id Image needs to be changed
        binding.btnChangeId.setOnClickListener {
            Snackbar.make(view,"saved",Snackbar.LENGTH_SHORT).show()
            person.id_approved = false
        }

        // Id Image OK
        binding.btnOkId.setOnClickListener {
            Snackbar.make(view,"saved",Snackbar.LENGTH_SHORT).show()
            person.id_approved = true
        }

        // General Not Approve
        binding.btnNotApproved.setOnClickListener {
            Snackbar.make(view,"saved",Snackbar.LENGTH_SHORT).show()
            person.approved = false
        }

        // General Approve
        binding.btnApproved.setOnClickListener {
            Snackbar.make(view,"saved",Snackbar.LENGTH_SHORT).show()
            person.approved = true
        }

        // saving changes to DB
        binding.btnSaveReviewDetails.setOnClickListener {
            person.reviewed = true

            // Uploading Alert-Dialog
            val dialogBuilder = AlertDialog.Builder(activity!!)
            dialogBuilder.setMessage("Uploading...")
            val alert = dialogBuilder.create()
            alert.show()

            /** Firebase RealTime: START **/
            // Save User's data
            database = Firebase.database.reference
            database.child(person.user_id.toString()).setValue(person).addOnSuccessListener {
                Log.i("REVIEW Firebase REALTIME:", "Success")

                /** SUCCESS **/
                alert.dismiss() // stop showing loading-alert-dialog
                Snackbar.make(view,"Review updated successfully!",Snackbar.LENGTH_LONG).show()
                Log.i("REVIEW:", "UPDATE successful!")

            }.addOnFailureListener {
                alert.dismiss() // stop showing loading-alert-dialog
                Snackbar.make(view,"Review update Failed! Check your information!",Snackbar.LENGTH_LONG).show()
                Log.i("UPDATE Firebase REALTIME:", "Failed")
            }
            /** Firebase RealTime: END **/

        }

        return view
    }

    private fun loadUserDataFromDataBase(view: ScrollView, person: Person) {
        binding.editTextPersonNameReviewDetails.text = person.first_name
        binding.editTextPersonLastNameReviewDetails.text = person.last_name
        binding.editTextAgeReviewDetails.text = person.age.toString()
        binding.editTextPhoneReviewDetails.text = person.phone
        binding.editTextCityReviewDetails.text = person.city

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
            binding.proPicImageReviewDetails.background = null
            binding.proPicImageReviewDetails.setImageBitmap(bitmapPro)

            // retrieve and insert id image of user from Firebase Storage
            val storageRefId = FirebaseStorage.getInstance().reference.child("images/${person.id_image}")
            val localFileId = File.createTempFile("tempProfileImageId","jpeg")
            storageRefId.getFile(localFileId).addOnSuccessListener {
                val bitmapId = BitmapFactory.decodeFile(localFileId.absolutePath)
                binding.idPicImageReviewDetails.background = null
                binding.idPicImageReviewDetails.setImageBitmap(bitmapId)
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

    }


}