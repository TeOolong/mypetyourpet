package com.example.mypetyourpet.model

data class Request (
    val id : Long,
    val requesterId : String,
    val publisherId : String,
    val postPath : String
)