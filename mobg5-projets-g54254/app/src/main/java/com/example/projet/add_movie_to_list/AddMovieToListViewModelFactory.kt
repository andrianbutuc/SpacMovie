package com.example.projet.add_movie_to_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projet.home.Movie

class AddMovieToListViewModelFactory(private val movie: Movie): ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddMovieToListViewModel::class.java)) {
            return AddMovieToListViewModel(movie) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}