package com.example.seekingbabysitter.data

import android.util.Log
import com.example.seekingbabysitter.model.Person
import com.google.firebase.database.*

class DataSource {

    private lateinit var database : DatabaseReference
    private lateinit var userList : ArrayList<Person>

    private fun loadList() {
        userList = arrayListOf()

        database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(Person::class.java)
                        userList.add(user!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("DataSource Firebase Realtime:", "Failed!")
            }

        })
        Log.i("DataSource ", userList[0].user_id!!)
    }


    fun loadReviewList(): List<Person> {
        val filteredList = ArrayList<Person>()
        loadList()
        for (p in userList) {
            if (p.approved == false && p.reviewed == false) {
                filteredList.add(p)
            }
        }
        return filteredList
    }

}