package com.example.seekingbabysitter.data

import com.example.seekingbabysitter.model.Person

class DataSource {
    private fun loadList(): List<Person> {
        return listOf(
            Person("tony","stark",55,"0547361936","kafas","sf@walla.com","2342fsdf32",
                reviewed = false,
                approved = false,
                user_image = "R.drawable.tony_11",
                id_image = "R.drawable.tony_11"
            ),
            Person("capt","amer",22,"0547361936","herz","dd@gmail.com","62fsdf32",
                reviewed = true,
                approved = true,
                user_image = "R.drawable.captain_america_22",
                id_image = "R.drawable.captain_america_22"
            ),
            Person("thor","rrr",66,"0547361936","tel aviv","gap@gmail.com","4342fsdf32",
                reviewed = true,
                approved = true,
                user_image = "R.drawable.thor_33",
                id_image = "R.drawable.thor_33"
            )
        )
    }

    fun loadFilteredList(): List<Person> {
        val filteredList = ArrayList<Person>()
        for (p in loadList()) {
            if (p.approved == true && p.reviewed == true) {
                filteredList.add(p)
            }
        }
        return filteredList
    }

}