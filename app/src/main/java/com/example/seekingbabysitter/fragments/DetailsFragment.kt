package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.seekingbabysitter.databinding.FragmentDetailsBinding
import com.example.seekingbabysitter.model.Person
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.transition.MaterialFadeThrough

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough() // MaterialDesign Transition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val displayCharacter = args.babysitter // get data from HomeFragment

        // convert jsonString to Character model
        val mapper = jacksonObjectMapper()
        val person: Person = mapper.readValue(displayCharacter, Person::class.java)

        // insert in fragment objects
        binding.imageDetails.setImageResource(person.user_image)
        binding.fullNameDetails.text = ("Full Name: " + person.first_name + " " + person.last_name)
        binding.ageDetails.text = ("Age: " + person.age.toString())
        binding.phoneDetails.text = ("Phone: " + person.phone)
        binding.cityDetails.text = ("City: " + person.city)
        binding.emailDetails.text = ("Email: " + person.email)
        binding.userIdDetails.text = ("User Id: " + person.user_id)

        return view
    }

    
}