package com.example.projet.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projet.movieslists.MovieList
import com.example.projet.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private var _registered = MutableLiveData<Boolean?>()
    val registered: LiveData<Boolean?>
        get() = _registered

    var emailInput = MutableLiveData<String>()
    var passwordInput = MutableLiveData<String>()

    /**
     * Signs up the user.
     */
    fun registerUser() {
        Log.d("RegisterViewModel", "${emailInput.value.isNullOrBlank()}  et ${passwordInput.value.isNullOrBlank()}  ${!Utils.isValidEmail(
            emailInput.value!!
        )}")
        if (emailInput.value.isNullOrBlank() || passwordInput.value.isNullOrBlank() || !Utils.isValidEmail(
                emailInput.value!!
            )
        ) {
            Log.d("RegisterViewModel", "DocumentSnapshot successfully failed!")
            registerUserFailed()
            return
        }
        Log.d("RegisterViewModel", "DocumentSnapshot successfully here!")

        firebaseAuth.createUserWithEmailAndPassword(emailInput.value!!, passwordInput.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createUser()
                } else if (!task.isSuccessful) {
                    registerUserFailed()
                }
            }.addOnFailureListener {
                registerUserFailed()
            }
    }

    /**
     * Creates the user in users collection and sets [_registered] true on success, false otherwise.
     */
    private fun createUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val movieLists = mutableListOf<MovieList>()
        movieLists.add(MovieList("Favorite", mutableListOf()))
        val newUser = User(firebaseUser!!.uid, firebaseUser.email!!, movieLists)
        db.collection("users").document(firebaseUser.uid).set(newUser)
            .addOnSuccessListener {
                Log.d("RegisterViewModel", "DocumentSnapshot successfully written!")
                _registered.value = true
            }
            .addOnFailureListener { e ->
                Log.w("RegisterViewModel", "Error writing document", e)
                registerUserFailed()
            }
    }

    /**
     * Sets all needed data after sign up is failed.
     */
    private fun registerUserFailed() {
        _registered.value = false
    }

    /**
     * Resets all needed data after sign up is completed.
     */
    fun registerUserCompleted() {
        _registered.value = null
        emailInput.value = ""
        passwordInput.value = ""
    }
}