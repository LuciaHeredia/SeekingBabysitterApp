package com.example.seekingbabysitter.model

data class Person(
    val first_name: String? = null,
    val last_name: String? = null,
    val age: Long? = 0,
    val phone: String? = null,
    val city: String? = null,
    val email: String? = null,
    val user_id: String? = null,
    var reviewed: Boolean? = false,
    var approved: Boolean? = false,
    val profile_image: String? = null,
    val id_image: String? = null,
    var profile_approved: Boolean? = false,
    var id_approved: Boolean? = false
)