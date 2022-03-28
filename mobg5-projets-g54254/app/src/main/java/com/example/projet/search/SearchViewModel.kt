package com.example.projet.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.projet.home.Movie
import com.example.projet.util.ApiUtils.Companion.BASE_URL
import com.example.projet.util.ApiUtils.Companion.QUERY
import com.example.projet.util.ApiUtils.Companion.SEARCH_MOVIE
import com.example.projet.util.FirestoreUtils
import com.example.projet.util.Utils
import kotlinx.coroutines.launch

class SearchViewModel(private val queue: RequestQueue) : ViewModel() {
    private val _searchedMovies = MutableLiveData<MutableList<Movie>>()
    val searchedMovies: LiveData<MutableList<Movie>>
        get() = _searchedMovies

    private lateinit var apiKey: String

    init {
        viewModelScope.launch {
            apiKey = "api_key=${
                FirestoreUtils.getApiKeyFireStore()?.documents?.get(0)?.getString("movie_db_key")
            }"
        }
    }

    /**
     * Search all movies with [movieName] string in title and update the [_searchedMovies].
     *
     */
    fun searchMovies(movieName: String) {
        if (movieName.isBlank()) {
            _searchedMovies.value = mutableListOf()
            return
        }
        val url = BASE_URL + SEARCH_MOVIE + apiKey + QUERY + movieName
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                _searchedMovies.value = Utils.populateList(response)
            },
            {
                Log.i("SearchViewModel", "response - searched movies error ${it.message}")
            }
        )
        queue.add(jsonObjectRequest)
    }
}