package com.imamfrf.movielook.ui.main

import com.imamfrf.movielook.data.model.Movie

interface MainListener {
    fun onItemClick(movie: Movie)
}