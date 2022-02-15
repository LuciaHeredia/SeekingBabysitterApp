package com.example.seekingbabysitter.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seekingbabysitter.adapter.DataAdapter
import com.example.seekingbabysitter.databinding.FragmentListBinding
import com.example.seekingbabysitter.model.Person
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.database.*

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
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

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerview.layoutManager = LinearLayoutManager(activity) // vertical layout Manager

        // Loading list from Firebase Realtime
        userList = arrayListOf()
        database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(Person::class.java)
                        userList.add(user!!)
                    }
                }

                val filteredList = loadApprovedList(userList) // getting list of approved users only
                val adapter = DataAdapter(filteredList)
                binding.recyclerview.adapter = adapter // Setting Adapter with recyclerview
                adapter.setOnItemClickListener(object : DataAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {

                        //convert Person model to jsonString
                        val mapper = jacksonObjectMapper()
                        val jsonString = mapper.writeValueAsString(filteredList[position])

                        //SafeArgs + Navigation
                        val directions = ListFragmentDirections.actionListFragmentToDetailsFragment(jsonString)
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

    fun loadApprovedList(uList: List<Person>): List<Person> {
        val filteredList = ArrayList<Person>()
        for (p in uList) {
            if (p.approved == true && p.reviewed == true) {
                filteredList.add(p)
            }
        }
        return filteredList
    }
}