package com.example.projet.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projet.R
import com.example.projet.databinding.HomeViewMovieBinding
import com.example.projet.util.ApiUtils

class MovieAdapter(private val context: Context, private val apiKey: String) :
    ListAdapter<Movie, MovieAdapter.MovieImageViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieImageViewHolder {
        return MovieImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieImageViewHolder, position: Int) {
        val item = getItem(position)
        ApiUtils.downloadImage(context, item, holder, apiKey)

        holder.binding.qualityImage.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("movieId", item.movieId)
            it?.findNavController()?.navigate(R.id.detailFragment, bundle)
        }
    }

    class MovieImageViewHolder private constructor(val binding: HomeViewMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MovieImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    HomeViewMovieBinding.inflate(layoutInflater, parent, false)

                return MovieImageViewHolder(binding)
            }
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.movieId == newItem.movieId
    }


    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.movieImageUrl == newItem.movieImageUrl
    }
}
