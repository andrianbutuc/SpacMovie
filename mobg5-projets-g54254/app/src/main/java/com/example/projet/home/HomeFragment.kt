package com.example.projet.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.example.projet.R
import com.example.projet.databinding.FragmentHomeBinding
import com.example.projet.util.FirestoreUtils
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        val queue = Volley.newRequestQueue(context)

        val viewModelFactory = context?.let { HomeViewModelFactory(queue) }
        viewModel = viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            ).get(HomeViewModel::class.java)
        }!!

        binding.homeViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        addAdapter(binding.recyclerViewPopularMovies, viewModel.popularMovies)
        addAdapter(binding.recyclerViewTopRatedMovies, viewModel.topRatedMovies)
        addAdapter(binding.recyclerViewUpcomingMovies, viewModel.upcomingMovies)

        return binding.root
    }

    /**
     * Adds a view adapter for [recyclerView] and link it with [movies] live data.
     * @param recyclerView a recycler view.
     * @param movies a liva data of movies mutable list.
     */
    private fun addAdapter(
        recyclerView: RecyclerView,
        movies: LiveData<MutableList<Movie>>
    ) {
        lifecycleScope.launch {
            val apiKey = "api_key=${
                FirestoreUtils.getApiKeyFireStore()?.documents?.get(0)?.getString("movie_db_key")
            }"
            val adapter = context?.let { MovieAdapter(it, apiKey) }
            recyclerView.adapter = adapter
            movies.observe(viewLifecycleOwner, {
                it?.let {
                    adapter!!.submitList(it)
                }
            })
            recyclerView.layoutManager =
                GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        }
    }
}