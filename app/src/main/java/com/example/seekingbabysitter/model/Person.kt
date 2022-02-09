package com.example.seekingbabysitter.model

import androidx.annotation.DrawableRes

data class Person(
    val first_name: String,
    val last_name: String,
    val age: Int,
    val phone: String,
    val city: String,
    val email: String,
    val user_id: String,
    val password: String,
    val reviewed: Boolean,
    val approved: Boolean,
    @DrawableRes val user_image: Int,
    @DrawableRes val id_image: Int
)