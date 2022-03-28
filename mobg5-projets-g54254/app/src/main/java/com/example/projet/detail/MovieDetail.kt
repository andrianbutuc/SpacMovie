package com.example.projet.detail

data class MovieDetail(
    val movieId : Int,
    val movieTitle :String,
    val movieDescription: String,
    val movieImageUrl : String
){
    constructor():this(-1,"","","")
}
