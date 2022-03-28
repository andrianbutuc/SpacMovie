package com.example.projet.movieslists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projet.register.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MoviesListsViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid


    private val _movieLists = MutableLiveData<MutableList<MovieList>>()
    val movieLists: LiveData<MutableList<MovieList>>
        get() = _movieLists

    init {
        getMovieLists()
    }

    /**
     * Gets all movie lists and update [_movieLists].
     */
    private fun getMovieLists() {
        val usersRef = db.collection("users").document(userId)
        usersRef.get().addOnSuccessListener {

            val user = it.toObject(User::class.java)
            _movieLists.value = user?.movieLists
            Log.d("MoviesListsViewModel", "Transaction success!")

        }.addOnFailureListener { e ->
            Log.w("MoviesListsViewModel", "Transaction failure.", e)
        }
    }

    /**
     * Refresh the movie lists.
     */
    fun refreshMovieLists() {
        getMovieLists()
    }
}
