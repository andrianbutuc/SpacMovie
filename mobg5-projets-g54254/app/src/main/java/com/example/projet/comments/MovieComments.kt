package com.example.projet.comments
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class MovieComments(
    val movieId:Int,

    @Contextual
    val comments: MutableList<Comment>
){
    constructor():this(-1,mutableListOf<Comment>())
}
