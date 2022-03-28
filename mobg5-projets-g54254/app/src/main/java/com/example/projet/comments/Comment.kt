package com.example.projet.comments
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val writtenBy:String,
    val content:String,

    ){
    constructor():this("","")
}
