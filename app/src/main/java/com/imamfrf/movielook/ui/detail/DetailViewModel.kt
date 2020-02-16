package com.imamfrf.movielook.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.imamfrf.movielook.data.MovieLookRepository
import com.imamfrf.movielook.data.model.Movie

class DetailViewModel(private val movieLookRepository: MovieLookRepository) : ViewModel() {

    fun getMovieDetails(id: String): LiveData<Movie>{
        Log.d("tes123", "id = $id")

        return movieLookRepository.getMovieDetails(id)
    }
    fun getMovieCredits(movie: Movie): LiveData<Movie>{
        return movieLookRepository.getMovieCredits(movie)
    }
    fun getMovieTrailer(movie: Movie): LiveData<Movie>{
        return movieLookRepository.getMovieTrailer(movie)
    }
    suspend fun addToFavorite(movie: Movie) = movieLookRepository.insertFavoriteMovie(movie)
    suspend fun deleteFromFavorite(movie: Movie) = movieLookRepository.deleteFavoriteMovie(movie)
    suspend fun getFavoriteMovie(id: String) = movieLookRepository.getFavoriteMovie(id)
}