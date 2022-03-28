package com.example.projet.util

import android.util.Patterns
import com.example.projet.home.Movie
import org.json.JSONObject


class Utils {
    companion object {
        /**
         * Checks if the given string is a valid email.
         * @param email a string.
         * @return true if the given string is a valid email, false otherwise.
         */
        fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        /**
         * Returns a mutable list of movies populated with data from [apiResponse].
         * [apiResponse] contains data from an api call.
         *
         * @param apiResponse a JSONObject.
         * @return a mutable list of movies.
         */
        fun populateList(apiResponse: JSONObject): MutableList<Movie> {
            val movieList = mutableListOf<Movie>()
            val movies = apiResponse.getJSONArray("results")

            for (i in 0 until movies.length()) {
                val item = movies.getJSONObject(i)

                movieList.add(
                    Movie(
                        item.getInt("id"),
                        item.getString("poster_path")
                    )
                )
            }

            return movieList
        }

    }
}
