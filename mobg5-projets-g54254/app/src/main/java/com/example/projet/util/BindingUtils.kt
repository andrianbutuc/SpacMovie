package com.example.projet.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.projet.comments.Comment
import com.example.projet.movieslists.MovieList

@BindingAdapter("listName")
fun TextView.setListName(item: MovieList) {
    text = item.listName
}
@BindingAdapter("writtenBy")
fun TextView.setWrittenBy(item: Comment) {
    text = item.writtenBy
}
@BindingAdapter("content")
fun TextView.setContent(item: Comment) {
    text = item.content
}