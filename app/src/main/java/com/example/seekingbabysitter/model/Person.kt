package com.example.seekingbabysitter.model

import androidx.annotation.DrawableRes

data class Person(
    val first_name: String,
    val last_name: String,
    val age: Int,
    val phone: String,
    val city: String,
    val facebook: String,
    val user_id: String,
    @DrawableRes val user_image: Int,
    @DrawableRes val id_image: Int
)