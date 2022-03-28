package com.example.projet.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projet.R
import com.example.projet.databinding.FragmentCommentsBinding

class CommentsFragment : Fragment() {

    private lateinit var binding: FragmentCommentsBinding
    private lateinit var viewModel: CommentsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_comments,
            container,
            false
        )

        val viewModelFactory =
            context?.let { CommentsViewModelFactory(requireArguments().getInt("movieId")) }

        viewModel = viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            ).get(CommentsViewModel::class.java)
        }!!

        binding.commentsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = context?.let { MovieCommentsAdapter() }

        binding.recyclerViewComments.adapter = adapter

        viewModel.movieComments.observe(viewLifecycleOwner, {
            it?.let {
                adapter!!.submitList(it)
            }
        })

        binding.recyclerViewComments.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        return binding.root
    }

}