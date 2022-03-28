package com.example.projet.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projet.databinding.MovieCommentsViewBinding

class MovieCommentsAdapter : ListAdapter<Comment, MovieCommentsAdapter.MovieCommentsViewHolder>(
    MovieCommentsDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCommentsViewHolder {
        return MovieCommentsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieCommentsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MovieCommentsViewHolder private constructor(val binding:  MovieCommentsViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Comment) {
            binding.comment = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MovieCommentsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieCommentsViewBinding.inflate(layoutInflater, parent, false)

                return MovieCommentsViewHolder(binding)
            }
        }
    }
}

class MovieCommentsDiffCallback : DiffUtil.ItemCallback<Comment>() {

    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.writtenBy == newItem.writtenBy && oldItem.content == newItem.content
    }


    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.content == newItem.content
    }
}
