package com.example.mypetyourpet.model

data class Post (
    val id : Long,
    val name : String,
    val species : String,
    val description : String,
    val image : String,
    val state : String,
    val userId : String
)