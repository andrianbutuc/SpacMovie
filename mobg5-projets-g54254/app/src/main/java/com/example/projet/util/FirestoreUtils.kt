package com.example.projet.util

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreUtils {
    companion object {

        /**
         * Gets the QuerySnapshot that contains the document with the key to themoviedb.org api
         * from database.
         *
         * @return QuerySnapshot contains the document with the key
         */
        suspend fun getApiKeyFireStore(): QuerySnapshot? {
            return try {
                val data = Firebase.firestore
                    .collection("apikey")
                    .get()
                    .await()
                data
            } catch (e: Exception) {
                null
            }
        }

    }
}