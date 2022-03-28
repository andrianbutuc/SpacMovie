package com.example.projet.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.projet.register.User
import com.example.projet.util.ApiUtils.Companion.BASE_URL
import com.example.projet.util.ApiUtils.Companion.DETAIL_MOVIE
import com.example.projet.util.FirestoreUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class DetailViewModel(private val queue: RequestQueue, private val movieId: Int) : ViewModel() {
    private val db = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser!!

    private val _movieDetails = MutableLiveData<MovieDetail>()
    val movieDetails: LiveData<MovieDetail>
        get() = _movieDetails

    private val _listNames = MutableLiveData<MutableList<String>>()
    val listNames: LiveData<MutableList<String>>
        get() = _listNames

    private lateinit var apiKey: String

    init {
        initializeViewModel()
    }

    /**
     * Initializes all data in view model.
     */
    private fun initializeViewModel() {
        viewModelScope.launch {
            apiKey = "api_key=${
                FirestoreUtils.getApiKeyFireStore()?.documents?.get(0)?.getString("movie_db_key")
            }"
            _listNames.value = getUserMovieList()
            getDetails()
        }
    }

    /**
     * Gets the movie detail using [movieId] attribute.
     */
    private fun getDetails() {
        val url = "$BASE_URL$DETAIL_MOVIE$movieId?$apiKey"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                _movieDetails.value = MovieDetail(
                    response.getInt("id"),
                    response.getString("original_title"),
                    response.getString("overview"),
                    response.getString("poster_path")
                )
                Log.i("DetailViewModel", "response detail movies - ${_movieDetails.value}")

            },
            { e ->
                Log.i("DetailViewModel", "response - detail movies error ${e.message}")
            }
        )
        queue.add(jsonObjectRequest)
    }

    /**
     * Gets movie lists of the current user and returns a mutable list of string with his
     * lists names.
     *
     * @return a mutable list of string with user's lists names.
     */
    private fun getUserMovieList(): MutableList<String> {

        val names = mutableListOf<String>()

        db.collection("users").document(user.uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documentSnapshot: DocumentSnapshot = task.result!!
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    val moviesLists = user!!.movieLists
                    for (m in moviesLists) {
                        names.add(m.listName)
                    }
                }
            }
        }
        return names
    }
}