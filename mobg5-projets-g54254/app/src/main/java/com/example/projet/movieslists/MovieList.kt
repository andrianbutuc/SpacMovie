package com.example.projet.movieslists

import com.example.projet.home.Movie
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class MovieList(

    val listName: String,

    @Contextual
    val movies: MutableList<Movie>,
) {
    constructor() : this(
        "", mutableListOf<Movie>()
    )
}
