package com.example.projet.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue

class HomeViewModelFactory(private val queue: RequestQueue): ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(queue) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}