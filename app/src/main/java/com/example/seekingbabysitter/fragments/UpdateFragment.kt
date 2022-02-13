package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.databinding.FragmentApplyBinding
import com.example.seekingbabysitter.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonSave.setOnClickListener {

            /*
            //convert Person model to jsonString
            val mapper = jacksonObjectMapper()
            val jsonString = mapper.writeValueAsString(person)

            //SafeArgs + Navigation
            val directions = LogInFragmentDirections.actionLoginFragmentToUserFragment(jsonString)
            findNavController().navigate(directions)
            */

            findNavController().navigate(R.id.action_updateFragment_to_userFragment)
        }

        return view
    }

}