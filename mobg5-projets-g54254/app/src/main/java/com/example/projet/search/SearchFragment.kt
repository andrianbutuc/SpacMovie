package com.example.projet.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.toolbox.Volley
import com.example.projet.R
import com.example.projet.databinding.FragmentSearchBinding
import com.example.projet.home.MovieAdapter
import com.example.projet.util.FirestoreUtils
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )
        val queue = Volley.newRequestQueue(context)

        val viewModelFactory = context?.let { SearchViewModelFactory(queue) }
        viewModel = viewModelFactory.let {
            ViewModelProvider(
                this,
                it!!
            ).get(SearchViewModel::class.java)
        }

        binding.searchViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.searchView.setOnQueryTextListener(onSearchView())
        val apiKey: String = lifecycleScope.launch {
            "api_key=${
                FirestoreUtils.getApiKeyFireStore()?.documents?.get(0)?.getString("movie_db_key")
            }"
        }.toString()

        val adapterRecyclerViewPopularMovies = context?.let { MovieAdapter(it, apiKey) }

        binding.recyclerViewSearchedMovies.adapter = adapterRecyclerViewPopularMovies

        viewModel.searchedMovies.observe(viewLifecycleOwner, {
            it?.let {
                adapterRecyclerViewPopularMovies!!.submitList(it)
            }
        })

        binding.recyclerViewSearchedMovies.layoutManager =
            GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)

        return binding.root
    }

    /**
     * Returns the query text listener object for a search view.
     *
     * @return the query text listener object.
     */
    private fun onSearchView() = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            return onQueryTextChange(query)
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            Log.i("SearchFragment", "response onQueryTextChange")
            if (newText != null) {
                viewModel.searchMovies(newText)
            }
            return false
        }
    }
}
