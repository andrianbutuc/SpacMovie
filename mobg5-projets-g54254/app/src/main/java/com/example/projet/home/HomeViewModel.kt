package com.example.projet.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.projet.util.ApiUtils.Companion.BASE_URL
import com.example.projet.util.ApiUtils.Companion.POPULAR_MOVIES
import com.example.projet.util.ApiUtils.Companion.TOP_RATED_MOVIES
import com.example.projet.util.ApiUtils.Companion.UPCOMING_MOVIES
import com.example.projet.util.FirestoreUtils
import com.example.projet.util.Utils
import kotlinx.coroutines.launch


class HomeViewModel(private val queue: RequestQueue) : ViewModel() {
    private val _popularMovies = MutableLiveData<MutableList<Movie>>()
    val popularMovies: LiveData<MutableList<Movie>>
        get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<MutableList<Movie>>()
    val topRatedMovies: LiveData<MutableList<Movie>>
        get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<MutableList<Movie>>()
    val upcomingMovies: LiveData<MutableList<Movie>>
        get() = _upcomingMovies

    lateinit var apiKey: String

    init {
        viewModelScope.launch {
            apiKey = "api_key=${
                FirestoreUtils.getApiKeyFireStore()?.documents?.get(0)?.getString("movie_db_key")
            }"
            Log.i("HomViewModel", "response  key - $apiKey")
            setMoviesFromApi(POPULAR_MOVIES, _popularMovies)
            setMoviesFromApi(TOP_RATED_MOVIES, _topRatedMovies)
            setMoviesFromApi(UPCOMING_MOVIES, _upcomingMovies)
        }
    }

    /**
     * Updates [moviesList] with [movieType] movies from an api call.
     */
    private fun setMoviesFromApi(
        movieType: String,
        moviesList: MutableLiveData<MutableList<Movie>>
    ) {
        val url = BASE_URL + movieType + apiKey
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                moviesList.value = Utils.populateList(response)
            },
            { e ->
                Log.i("HomViewModel", "response - popular movies error ${e.message}")
            }
        )
        queue.add(jsonObjectRequest)
    }
}