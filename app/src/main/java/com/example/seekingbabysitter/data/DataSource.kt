package com.example.seekingbabysitter.data

import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.model.Person

class DataSource {
    fun loadCharacters(): List<Person> {
        return listOf(
            Person("tony","stark",55,"0547361936","kafas","facebook","2342fsdf32",
                R.drawable.tony_11, R.drawable.tony_11),
            Person("capt","amer",22,"0547361936","herz","facebook","62fsdf32",
                R.drawable.captain_america_22,R.drawable.captain_america_22),
            Person("thor","rrr",66,"0547361936","tel aviv","facebook","4342fsdf32",
                R.drawable.thor_33,R.drawable.thor_33)
        )
    }
}