package com.example.projet.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue

class SearchViewModelFactory(private val queue: RequestQueue): ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(queue) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}