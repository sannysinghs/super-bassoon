package com.tixeon.simplewebcrawler.ui.movies

import androidx.lifecycle.ViewModel
import com.tixeon.simplewebcrawler.domain.model.MovieItem
import com.tixeon.simplewebcrawler.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
): ViewModel() {

    private val _similarMovies = MutableStateFlow(emptyList<MovieItem>())

    val similarMovies: Flow<List<MovieItem>> = _similarMovies


    init {
        fetchRequests()
    }

    private fun fetchRequests() {

    }
}


