package com.example.projet.detail

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.android.volley.toolbox.Volley
import com.example.projet.R
import com.example.projet.comments.CommentsFragment
import com.example.projet.databinding.FragmentDetailBinding
import com.example.projet.home.Movie
import com.example.projet.util.ApiUtils
import com.example.projet.util.FirestoreUtils
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )
        // this is not the best solution for this view but is the shortest. Must be improved
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val args = DetailFragmentArgs.fromBundle(requireArguments())
        val queue = Volley.newRequestQueue(context)

        val viewModelFactory = context?.let { DetailViewModelFactory(queue, args.movieId) }
        viewModel = viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            ).get(DetailViewModel::class.java)
        }!!

        binding.detailViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.movieDetails.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                val apiKey = "api_key=${
                    FirestoreUtils.getApiKeyFireStore()?.documents?.get(0)
                        ?.getString("movie_db_key")
                }"
                val movie = Movie(it.movieId, it.movieImageUrl)
                ApiUtils.downloadImage(requireContext(), movie, binding.imageViewDetail, apiKey)
            }
            binding.title.text = it.movieTitle
            binding.description.text = it.movieDescription
        })

        binding.addButton.setOnClickListener {
            val nameList = viewModel.listNames.value
            if (nameList != null) {
                view?.findNavController()?.navigate(
                    DetailFragmentDirections.actionDetailFragmentToAddMovieToListFragment(
                        nameList.toTypedArray(),
                        viewModel.movieDetails.value!!.movieId,
                        viewModel.movieDetails.value!!.movieImageUrl
                    )
                )
            }
        }

        requireActivity().supportFragmentManager.commit {
            val bundle = Bundle()
            bundle.putInt("movieId", args.movieId)
            add<CommentsFragment>(R.id.commentsFragment, "CommentsFragment", bundle)
        }

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}