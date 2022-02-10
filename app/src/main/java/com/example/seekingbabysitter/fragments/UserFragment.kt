package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.databinding.FragmentUserBinding
import com.example.seekingbabysitter.model.Person
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val args: UserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        val displayUser = args.babysitterLogged // get data from LogInFragment

        // convert jsonString to Character model
        val mapper = jacksonObjectMapper()
        val person: Person = mapper.readValue(displayUser, Person::class.java)

        // update name text
        val fullName = person.first_name + " " + person.last_name
        binding.userTextLogged.text = ("$fullName is logged in!")

        // update button name
        var btnName = "UPDATE PROFILE"
        if(person.user_id == "manager")
            btnName = "MANAGER"
        binding.loggedBtnUser.text = btnName

        binding.listBtnUser.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_listFragment)
        }

        binding.loggedBtnUser.setOnClickListener {
            if (btnName == "UPDATE PROFILE")
                findNavController().navigate(R.id.action_userFragment_to_updateFragment)
        }

        return view
    }

}