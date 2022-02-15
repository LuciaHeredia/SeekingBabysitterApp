package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seekingbabysitter.adapter.DataAdapter
import com.example.seekingbabysitter.databinding.FragmentReviewListBinding
import com.example.seekingbabysitter.model.Person
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.database.*

class ReviewListFragment : Fragment() {
    private var _binding: FragmentReviewListBinding? = null
    private val binding get() = _binding!!

    private lateinit var database : DatabaseReference
    private lateinit var userList : ArrayList<Person>

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

        binding.recyclerviewReview.layoutManager = LinearLayoutManager(activity) // vertical layout Manager

        userList = arrayListOf()
        database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Person::class.java)
                        userList.add(user!!)
                    }
                }

                val reviewList = loadReviewList(userList) // getting list of users to review only
                val adapter = DataAdapter(reviewList)
                binding.recyclerviewReview.adapter = adapter // Setting Adapter with recyclerview
                adapter.setOnItemClickListener(object : DataAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {

                        //convert Person model to jsonString
                        val mapper = jacksonObjectMapper()
                        val jsonString = mapper.writeValueAsString(reviewList[position])

                        //SafeArgs + Navigation
                        val directions = ReviewListFragmentDirections.actionReviewListFragmentToReviewDetailsFragment(jsonString)
                        findNavController().navigate(directions)

                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("DataSource Firebase Realtime:", "Failed!")
            }

        })

        return view
    }

    fun loadReviewList(uList: List<Person>): List<Person> {
        val reviewList = ArrayList<Person>()
        for (p in uList) {
            if (p.approved == false && p.reviewed == false && p.user_id != "manager") {
                reviewList.add(p)
            }
        }
        return reviewList
    }

}