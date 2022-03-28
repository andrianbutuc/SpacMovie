package com.example.projet.add_new_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.projet.R
import com.example.projet.databinding.FragmentAddNewListOfMoviesBinding

class AddNewMovieListFragment : DialogFragment() {

    private lateinit var binding: FragmentAddNewListOfMoviesBinding
    private lateinit var viewModel: AddNewMovieListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_new_list_of_movies,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(AddNewMovieListViewModel::class.java)
        binding.addNewMovieListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.succeed.observe(viewLifecycleOwner, { succeed ->
            if (succeed != null) {
                if (succeed) {
                    setFragmentResult("dialog", bundleOf("succeed" to succeed))
                }
                dismiss()
            }
        })

        return binding.root
    }
}