package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seekingbabysitter.adapter.DataAdapter
import com.example.seekingbabysitter.data.DataSource
import com.example.seekingbabysitter.databinding.FragmentReviewListBinding
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.transition.MaterialFadeThrough

class ReviewListFragment : Fragment() {
    private var _binding: FragmentReviewListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialFadeThrough() // MaterialDesign Transition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReviewListBinding.inflate(inflater, container, false)
        val view = binding.root

        val myDataset = DataSource().loadFilteredList()
        binding.recyclerviewReview.layoutManager = LinearLayoutManager(activity) // vertical layout Manager
        val adapter = DataAdapter(myDataset)
        binding.recyclerviewReview.adapter = adapter // Setting Adapter with recyclerview
        adapter.setOnItemClickListener(object : DataAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                //convert Person model to jsonString
                val mapper = jacksonObjectMapper()
                val jsonString = mapper.writeValueAsString(myDataset[position])

                //SafeArgs + Navigation
                val directions = ReviewListFragmentDirections.actionReviewListFragmentToReviewDetailsFragment(jsonString)
                findNavController().navigate(directions)

            }
        })

        return view
    }
}