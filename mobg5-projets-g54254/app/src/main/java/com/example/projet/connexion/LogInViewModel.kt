package com.example.projet.connexion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projet.util.Utils
import com.google.firebase.auth.FirebaseAuth

class LogInViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var _connected = MutableLiveData<Boolean?>()
    val connected: LiveData<Boolean?>
        get() = _connected

    var emailInput = MutableLiveData<String>()
    var passwordInput = MutableLiveData<String>()

    /**
     * Logs in the user.
     */
    fun logInUser() {
        if (emailInput.value.isNullOrBlank() || passwordInput.value.isNullOrBlank() || !Utils.isValidEmail(
                emailInput.value!!
            )
        ) {
            logInUserFailed()
            return
        }
        firebaseAuth.signInWithEmailAndPassword(emailInput.value!!, passwordInput.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null && task.result!!.user != null) {
                    _connected.value = true
                } else {
                    logInUserFailed()
                }
            }.addOnFailureListener {
                logInUserFailed()
            }
    }

    /**
     * Sets all needed data after log in is failed.
     */
    private fun logInUserFailed() {
        _connected.value = false
    }

    /**
     * Resets all needed data after log in is completed.
     */
    fun logInUserComplete() {
        _connected.value = null
        emailInput.value = ""
        passwordInput.value = ""
    }
}
