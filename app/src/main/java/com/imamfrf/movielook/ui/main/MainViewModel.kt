package com.imamfrf.movielook.ui.main

import androidx.lifecycle.ViewModel
import com.imamfrf.movielook.data.MovieLookRepository

class MainViewModel(movieLookRepository: MovieLookRepository) : ViewModel() {
    val nowPlayingMovie = movieLookRepository.getNowPlaying()
    val popularMovie = movieLookRepository.getPopular()
    val favoritesMovie = movieLookRepository.getAllFavoriteMovie()
}