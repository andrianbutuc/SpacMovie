package com.example.projet.movieslists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projet.R
import com.example.projet.databinding.FragmentMoviesListsBinding
import com.example.projet.util.FirestoreUtils
import kotlinx.coroutines.launch

class MoviesListsFragment : Fragment() {
    private lateinit var binding: FragmentMoviesListsBinding
    private var movieListsUpdated : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_movies_lists,
            container,
            false
        )
        val viewModel = ViewModelProvider(this).get(MoviesListsViewModel::class.java)

        binding.movieListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val apiKey: String = lifecycleScope.launch {
            "api_key=${
                FirestoreUtils.getApiKeyFireStore()?.documents?.get(0)?.getString("movie_db_key")
            }"
        }.toString()
        val adapter = context?.let { MovieListAdapter(it, apiKey) }

        binding.recyclerViewMovieList.adapter = adapter

        viewModel.movieLists.observe(viewLifecycleOwner, {
            it?.let {
                adapter!!.submitList(it)
            }
        })
        binding.recyclerViewMovieList.layoutManager =
            GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)

        setFragmentResultListener("dialog") { requestKey, bundle ->
            movieListsUpdated = bundle.getBoolean("succeed")

            if (movieListsUpdated){
                viewModel.refreshMovieLists()
                movieListsUpdated = false
            }

        }
        binding.addList.setOnClickListener {
            view?.findNavController()?.navigate(
                MoviesListsFragmentDirections.actionMoviesListsFragmentToAddNewMovieListFragment()
            )
        }

        return binding.root
    }

}