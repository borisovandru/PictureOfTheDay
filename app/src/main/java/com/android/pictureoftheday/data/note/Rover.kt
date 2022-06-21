package com.android.pictureoftheday.data.note

data class Rover(
    val name: String,
    val dateStart: String,
    val dateEnd: String,
    val cameras: List<String>
)