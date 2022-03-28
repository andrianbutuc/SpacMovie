package com.example.projet.add_movie_to_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.projet.R
import com.example.projet.databinding.FragmentAddMovieToListBinding
import com.example.projet.home.Movie

class AddMovieToListFragment : DialogFragment() {

    private lateinit var binding: FragmentAddMovieToListBinding
    private lateinit var viewModel: AddMovieToListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_movie_to_list,
            container,
            false
        )

        val nameList = requireArguments().getStringArray("nameList").orEmpty()

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line, nameList
        )
        binding.listView.adapter = adapter

        val movieId = requireArguments().getInt("movieId")
        val imageUrl = requireArguments().getString("movieImageUrl")

        val movie = Movie(movieId, imageUrl!!)

        val viewModelFactory = context?.let { AddMovieToListViewModelFactory(movie) }
        viewModel = viewModelFactory.let {
            ViewModelProvider(
                this,
                it!!
            ).get(AddMovieToListViewModel::class.java)
        }

        binding.addMovieToListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position)
                viewModel.addMovieToList(selectedItem.toString())
                dismiss()
            }

        return binding.root
    }
}