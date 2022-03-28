package com.example.projet.add_movie_to_list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projet.home.Movie
import com.example.projet.register.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddMovieToListViewModel(private val movie: Movie) : ViewModel() {

    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    /**
     * Adds the [movie] to the [nameList] list if does not exists already.
     * @param nameList the string name of the list.
     */
    fun addMovieToList(nameList: String) {
        val usersRef = db.collection("users").document(userId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(usersRef)
            val user = snapshot.toObject(User::class.java)
            val movieLists = user?.movieLists

            if (movieLists != null) {
                for (movieList in movieLists) {
                    if (movieList.listName == nameList && !exists(movieList.movies)) {
                        movieList.movies.add(movie)
                        transaction.update(usersRef, "movieLists", movieLists)
                        Log.d("AddNewMovieListViewMode", "$movie added to $nameList")
                    }
                }
            }

            // Success
            null
        }.addOnSuccessListener {
            Log.d("AddNewMovieListViewMode", "Transaction success!")
        }.addOnFailureListener { e ->
            Log.w("AddNewMovieListViewMode", "Transaction failure.", e)
        }
    }

    /**
     * Checks if the [movie] exists in [movies] list.
     * @param movies mutable list of movie.
     *
     * @return true if movie exist in list, false otherwise.
     */
    private fun exists(movies: MutableList<Movie>): Boolean {
        var exists = false
        for (movie in movies) {
            if (movie.movieId == this.movie.movieId) {
                exists = true
            }
        }
        return exists
    }
}