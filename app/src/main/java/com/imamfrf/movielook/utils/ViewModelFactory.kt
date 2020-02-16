package com.imamfrf.movielook.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imamfrf.movielook.data.MovieLookRepository
import com.imamfrf.movielook.data.local.LocalRepository
import com.imamfrf.movielook.data.local.room.MovieLookDatabase
import com.imamfrf.movielook.data.remote.RemoteRepository
import com.imamfrf.movielook.ui.detail.DetailViewModel
import com.imamfrf.movielook.ui.main.MainViewModel

class ViewModelFactory(private val movieLookRepository: MovieLookRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    MovieLookRepository.getInstance(
                        RemoteRepository.getInstance(
                            application
                        ),
                        LocalRepository.getInstance(MovieLookDatabase.getInstance(application).getMovieLookDao())
                    )
                ).also {
                    INSTANCE = it
                }
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                movieLookRepository
            ) as T

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(
                movieLookRepository
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}