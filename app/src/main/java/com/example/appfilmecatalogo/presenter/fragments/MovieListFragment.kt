package com.example.appfilmecatalogo.presenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appfilmecatalogo.R
import com.example.appfilmecatalogo.databinding.FragmentListBinding
import com.example.appfilmecatalogo.domain.models.Lives
import com.example.appfilmecatalogo.domain.utils.FilterTypes
import com.example.appfilmecatalogo.domain.utils.MovieResult
import com.example.appfilmecatalogo.presenter.adapters.MovieItemAdapter
import com.example.appfilmecatalogo.presenter.view.FactoryBuilder
import com.example.appfilmecatalogo.presenter.viewmodel.Movie.MovieDetailsViewModel
import com.example.appfilmecatalogo.presenter.viewmodel.Movie.MovieListViewModel

class MovieListFragment : Fragment(), View.OnClickListener {

    private val movielistAdapter = MovieItemAdapter()
    private val movieListViewModel: MovieListViewModel by activityViewModels { FactoryBuilder.movieFactory }
    private val movieDetailViewModel: MovieDetailsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentListBinding.inflate(layoutInflater)
        val view = binding.root

        binding.imgMenu.setOnClickListener(this)

        movielistAdapter.onClickListener = { movieId ->
            goToMovieDetails(movieId)
        }

        binding.movieItemRecyclerView.adapter = movielistAdapter
        movieListViewModel.getAllLives()
        getMovieAndObserve()

        return view
    }

    private fun goToMovieDetails(movieId: Int) {
        findNavController().navigate(R.id.listFragment_to_listDetail)
        movieListViewModel.movies.observe(viewLifecycleOwner) { movieresult ->
            if (movieresult is MovieResult.Sucess) {
                val movieselected = movieresult.data.results.find { PopularWeeklyFilms ->
                    PopularWeeklyFilms.id == movieId
                }
                movieselected?.let {
                    movieDetailViewModel.movieSelect(movieselected)
                }
            }
        }
    }

    private fun getMovieAndObserve() {
        movieListViewModel.movies.observe(viewLifecycleOwner) { movieApiResult ->
            when (movieApiResult) {
                is MovieResult.Loading -> {
                }
                is MovieResult.Sucess -> {
                    setListAdapter(movieApiResult.data)
                }
                is MovieResult.Error -> {
                    setListAdapter(movieApiResult.emptyLive)
                }
            }
        }
    }

    private fun setListAdapter(list: Lives) {
        movielistAdapter.submitList(list.results)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.img_menu) {
            val popupmenu = PopupMenu(context, view)
            popupmenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item1 -> {
                        movieListViewModel.movies.observe(viewLifecycleOwner) { moveApiResult ->
                            if (moveApiResult is MovieResult.Sucess) {
                                FilterTypes.POPULARITY.filterTypes(moveApiResult.data)
                            }
                        }
                        true
                    }
                    R.id.item2 -> {
                        movieListViewModel.movies.observe(viewLifecycleOwner) { moveApiResult ->
                            if (moveApiResult is MovieResult.Sucess)
                                FilterTypes.RELEASE_DATE.filterTypes(moveApiResult.data)
                        }
                        true
                    }
                    R.id.item3 -> {
                        movieListViewModel.movies.observe(viewLifecycleOwner) { moveApiResult ->
                            if (moveApiResult is MovieResult.Sucess)
                                FilterTypes.TITLE.filterTypes(moveApiResult.data)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupmenu.inflate(R.menu.menu_main)
            popupmenu.show()
        }
    }
}
