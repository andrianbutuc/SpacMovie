package com.example.projet.util

import android.content.Context
import android.widget.ImageView
import com.example.projet.home.Movie
import com.example.projet.home.MovieAdapter
import com.squareup.picasso.Picasso

class ApiUtils {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3"
        const val POPULAR_MOVIES = "/movie/popular?"
        const val TOP_RATED_MOVIES = "/movie/top_rated?"
        const val UPCOMING_MOVIES = "/movie/upcoming?"
        const val DETAIL_MOVIE = "/movie/"
        const val SEARCH_MOVIE = "/search/movie?"
        const val QUERY = "&query="
        private const val IMAGE_DOWNLOAD = "https://image.tmdb.org/t/p/original/"


        /**
         * Downloads the movie image into his holder.
         *
         * @param context application context.
         * @param movie a movie
         * @param holder image holder
         * @param apiKey key to the themoviedb.org api
         */
        fun downloadImage(
            context: Context, movie: Movie, holder: MovieAdapter.MovieImageViewHolder,
            apiKey: String
        ) {
            Picasso.with(context)
                .load("$IMAGE_DOWNLOAD${movie.movieImageUrl}?$apiKey")
                .into(holder.binding.qualityImage)
        }

        /**
         * Downloads the movie image into ImageView.
         *
         * @param context application context.
         * @param movie a movie
         * @param holder image view
         * @param apiKey key to the themoviedb.org api
         */
        fun downloadImage(
            context: Context, movie: Movie, holder: ImageView, apiKey: String
        ) {
            Picasso.with(context)
                .load("$IMAGE_DOWNLOAD${movie.movieImageUrl}?$apiKey")
                .into(holder)
        }
    }
}