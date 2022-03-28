package com.example.projet.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue

class DetailViewModelFactory(private val queue: RequestQueue,private val movieId: Int): ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(queue,movieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}