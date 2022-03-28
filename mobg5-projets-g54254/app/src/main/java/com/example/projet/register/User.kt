package com.example.projet.register

import com.example.projet.movieslists.MovieList

data class  User(
    val id:String,
    val email:String,
    val movieLists:MutableList<MovieList>
){
    constructor():this(
        "","",mutableListOf<MovieList>()
    )
}

