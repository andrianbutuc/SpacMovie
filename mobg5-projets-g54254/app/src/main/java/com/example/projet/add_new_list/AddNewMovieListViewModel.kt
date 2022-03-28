package com.example.projet.add_new_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projet.movieslists.MovieList
import com.example.projet.register.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddNewMovieListViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    private var _succeed = MutableLiveData<Boolean?>()
    val succeed: LiveData<Boolean?>
        get() = _succeed

    var newListName = MutableLiveData<String>()

    /**
     * Adds a new movie list for the current user.
     */
    fun addNewMovieList() {
        if (newListName.value.isNullOrBlank()) {
            addNewMovieListCancel()
            return
        }

        val usersRef = db.collection("users").document(userId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(usersRef)
            val user = snapshot.toObject(User::class.java)
            val movieLists = user?.movieLists

            if (!exists(movieLists)) {
                movieLists?.add(MovieList(newListName.value.toString(), mutableListOf()))
                transaction.update(usersRef, "movieLists", movieLists)
                Log.d("AddNewMovieListViewMode", "new list - ${newListName.value}")
            }

            // Success
            null
        }.addOnSuccessListener {
            _succeed.value = true
            Log.d("AddNewMovieListViewMode", "Transaction success!")
        }.addOnFailureListener { e ->
            addNewMovieListCancel()
            Log.w("AddNewMovieListViewMode", "Transaction failure.", e)
        }

    }

    /**
     * Checks if the movie list exists already.
     * @param movieLists mutable list of movie lists.
     *
     * @return true if movie list exist, false otherwise.
     */
    private fun exists(movieLists: MutableList<MovieList>?): Boolean {
        if (movieLists == null) {
            return false
        }
        var exists = false
        for (movieList in movieLists) {
            if (movieList.listName == newListName.value) {
                exists = true
            }
        }
        return exists
    }

    /**
     * Sets all needed data if cancel  the addition of a new movie list.
     */
    fun addNewMovieListCancel() {
        _succeed.value = false
    }
}