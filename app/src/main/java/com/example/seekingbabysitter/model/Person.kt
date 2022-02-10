package com.example.seekingbabysitter.model

data class Person(
    val first_name: String? = null,
    val last_name: String? = null,
    val age: Long? = 0,
    val phone: String? = null,
    val city: String? = null,
    val email: String? = null,
    val user_id: String? = null,
    val reviewed: Boolean? = false,
    val approved: Boolean? = false,
    val user_image: String? = null,
    val id_image: String? = null
)