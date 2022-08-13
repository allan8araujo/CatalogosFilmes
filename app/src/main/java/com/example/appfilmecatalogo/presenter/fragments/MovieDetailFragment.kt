package com.example.appfilmecatalogo.presenter.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.abstractions.models.PopularWeeklyFilms
import com.example.appfilmecatalogo.R
import com.example.appfilmecatalogo.databinding.FragmentMovieDetailBinding
import com.example.appfilmecatalogo.presenter.helpers.ImageDetailListener
import com.example.appfilmecatalogo.presenter.viewmodel.Movie.MovieDetailsViewModel
import com.example.appfilmecatalogo.presenter.viewmodel.Movie.MovieListViewModel

class MovieDetailFragment : Fragment() {

    private val movieDetailsViewModel: MovieDetailsViewModel by activityViewModels()
    private val movieListViewModel: MovieListViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMovieDetailBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.movieDescription.movementMethod = ScrollingMovementMethod()

        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.back_to_movieListFragment)
        }
        binding.movieImage.setOnClickListener {
            findNavController().navigate(R.id.movieDetailFragment_to_DetailImageFragment)
        }

        setDataAndObserve(binding)
        return view
    }

    private fun setDataAndObserve(binding: FragmentMovieDetailBinding) {
        movieListViewModel.mutableSelectedMovie.observe(viewLifecycleOwner) { movieSelected ->
            binding.textMovieTitleDetails.text = movieSelected?.title
            binding.releaseDate.text = "Release date: ${movieSelected?.release_date}"
            binding.voteAverage.text = movieSelected?.vote_average.toString()
            binding.shimmerMovieDetails.showShimmer(true)

            Glide.with(binding.root.context)
                .load(getPosterLink(movieSelected))
                //.placeholder(R.drawable.loading_details)
                .centerCrop()
                .listener(ImageDetailListener(binding.shimmerMovieDetails, movieDetailsViewModel))
                .into(binding.movieImage)
            //Todo add fun here
            val overview = movieSelected?.overview
            binding.movieDescription.text = overview
        }
    }

    private fun getPosterLink(movieSelected: PopularWeeklyFilms?) =
        "https://image.tmdb.org/t/p/original" + movieSelected?.backdrop_path
}
