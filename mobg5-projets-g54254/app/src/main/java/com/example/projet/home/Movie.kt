package com.example.projet.home
import kotlinx.serialization.Serializable

@Serializable
data class Movie (
        val movieId : Int,
        val movieImageUrl : String
){
        constructor():this(
                -1,""
        )
}
