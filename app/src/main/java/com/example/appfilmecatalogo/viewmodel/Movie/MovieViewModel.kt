package com.example.appfilmecatalogo.viewmodel.Movie

import androidx.lifecycle.*
import com.example.appfilmecatalogo.models.Lives
import com.example.appfilmecatalogo.models.MovieResult
import com.example.appfilmecatalogo.models.mockLives
import com.example.appfilmecatalogo.models.movieDetails
import com.example.appfilmecatalogo.repository.IMovieRepository
import com.example.appfilmecatalogo.repository.IMovieRepositoryDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MovieViewModel(
    private val movieRepository: IMovieRepository,
) : ViewModel() {
    private val livelist = MutableLiveData<MovieResult<Lives>>()
    val movies: LiveData<MovieResult<Lives>> = livelist

    fun getAllLives() {
        viewModelScope.launch {
            livelist.value = MovieResult.Loading()
            try {
                val movieFromApi = withContext(Dispatchers.IO) {
                    movieRepository.getAllLives()
                }
                livelist.value = MovieResult.Sucess(movieFromApi)
            } catch (e: Exception) {
                val movieResult = MovieResult.Error<Lives>(e, mockLives())

                livelist.value=movieResult
            }
        }
    }
}

class MovieViewModelDetails(
    private val movieRepositoryDetails: IMovieRepositoryDetails,
) : ViewModel() {
    private val detailMovie = MutableLiveData<movieDetails>()
    val movies: LiveData<movieDetails> = detailMovie

    fun getAllLivesDetails() {
        viewModelScope.launch {
            val movieFromApiDetails = withContext(Dispatchers.IO) {
                movieRepositoryDetails.getMovieDetail()
            }
            detailMovie.value = movieFromApiDetails
        }
    }
}
