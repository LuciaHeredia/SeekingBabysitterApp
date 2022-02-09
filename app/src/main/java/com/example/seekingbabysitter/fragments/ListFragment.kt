package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.adapter.DataAdapter
import com.example.seekingbabysitter.data.DataSource
import com.example.seekingbabysitter.databinding.FragmentListBinding
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.card.MaterialCardView

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        val myDataset = DataSource().loadFilteredList()
        binding.recyclerview.layoutManager = LinearLayoutManager(activity) // vertical layout Manager
        val adapter = DataAdapter(myDataset)
        binding.recyclerview.adapter = adapter // Setting Adapter with recyclerview
        adapter.setOnItemClickListener(object : DataAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                //convert Person model to jsonString
                val mapper = jacksonObjectMapper()
                val jsonString = mapper.writeValueAsString(myDataset[position])

                //MaterialDesign Transition + SafeArgs + Navigation
                val cardView = view.findViewById<MaterialCardView>(R.id.details_view)
                val transitionName = getString(R.string.person_card_transition)
                val extras = FragmentNavigatorExtras(cardView to transitionName)
                val directions = ListFragmentDirections.actionListFragmentToDetailsFragment(jsonString)
                findNavController().navigate(directions,extras)

            }
        })


        return view
    }
}