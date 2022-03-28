package com.example.projet.movieslists

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projet.databinding.MovieListViewBinding
import com.example.projet.home.MovieAdapter

class MovieListAdapter(private val context: Context, private val apiKey: String) :
    ListAdapter<MovieList, MovieListAdapter.MovieListViewHolder>(MovieListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        val adapter = MovieAdapter(context, apiKey)
        adapter.submitList(item.movies)
        holder.binding.imageViewMovieList.adapter = adapter
        holder.binding.imageViewMovieList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    class MovieListViewHolder private constructor(val binding: MovieListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieList) {
            binding.movieList = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MovieListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListViewBinding.inflate(layoutInflater, parent, false)

                return MovieListViewHolder(binding)
            }
        }
    }
}

class MovieListDiffCallback : DiffUtil.ItemCallback<MovieList>() {

    override fun areItemsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
        return oldItem.listName == newItem.listName
    }


    override fun areContentsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
        return oldItem == newItem
    }
}
