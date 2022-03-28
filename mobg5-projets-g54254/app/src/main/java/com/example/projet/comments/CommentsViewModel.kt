package com.example.projet.comments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentsViewModel(private val movieId: Int) :ViewModel() {
    private val db = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser!!

    private val _movieComments = MutableLiveData<MutableList<Comment>>()
    val movieComments : LiveData<MutableList<Comment>>
        get() = _movieComments

    var comment = MutableLiveData<String>()

    init {
        getComments()
    }

    /**
     * Gets all the comments for the movie with id [movieId] into [_movieComments].
     */
    private fun getComments(){
        val commentRef = db.collection("comments").document(movieId.toString())
        commentRef.get()
            .addOnSuccessListener {
                Log.d("CommentsViewModel", "Transaction success!")
                val comments = it.toObject(MovieComments::class.java)
                Log.d("CommentsViewModel", "$comments")
                _movieComments.value= comments?.comments

            }.addOnFailureListener { e ->
                Log.w("CommentsViewModel", "Transaction failure.", e)
            }
    }

    /**
     * Add a comment to the movie.
     */
    fun addComment(){
        if(comment.value.isNullOrBlank() || user.email.isNullOrBlank()){
            return
        }
        val commentValue = comment.value!!

        val commentRef = db.collection("comments").document(movieId.toString())
        commentRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    val newComment = Comment(user.email!!, commentValue)
                    if (document.exists()) {
                        db.runTransaction { transaction ->
                            val snapshot = transaction.get(commentRef)
                            val movieComments = snapshot.toObject(MovieComments::class.java)
                            val comments = movieComments?.comments
                            comments?.add(newComment)
                            transaction.update(commentRef, "comments", comments)
                            // Success
                            null
                        }.addOnSuccessListener {
                            _movieComments.value?.add(newComment)
                            Log.d("CommentsViewModel", "Transaction success!")
                        }.addOnFailureListener { e ->
                            Log.w("CommentsViewModel", "Transaction failure.", e)
                        }
                    } else {
                        val comments = mutableListOf<Comment>()
                        comments.add(newComment)
                        commentRef.set(hashMapOf("comments" to comments))
                            .addOnSuccessListener {
                                _movieComments.value?.add(newComment)
                                Log.d("CommentsViewModel", "Transaction success!")
                            }.addOnFailureListener { e ->
                                Log.w("CommentsViewModel", "Transaction failure.", e)
                            }
                    }
                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }
        comment.value = ""
    }
}