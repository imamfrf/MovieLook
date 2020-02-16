package com.imamfrf.movielook.data.remote

import android.app.Application
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.imamfrf.movielook.data.model.Cast
import com.imamfrf.movielook.data.model.Movie
import com.imamfrf.movielook.utils.Constants.API_KEY
import com.imamfrf.movielook.utils.Constants.URL_DISCOVER
import com.imamfrf.movielook.utils.Constants.URL_GET_MOVIE
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class RemoteRepository(private val application: Application) {

    companion object {

        @Volatile
        private var INSTANCE: RemoteRepository? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RemoteRepository(application).also {
                    INSTANCE = it
                }
            }
    }

    fun getNowPlaying(callback: GetNowPlayingCallback) {
        val calendarEnd = Calendar.getInstance()
        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DATE, -30)
        val endDate = calendarEnd.time
        val startDate = calendarStart.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val endString = dateFormat.format(endDate)
        val startString = dateFormat.format(startDate)
        val movies = arrayListOf<Movie>()

        AndroidNetworking.get(URL_DISCOVER)
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("page", "1")
            .addQueryParameter("primary_release_date.gte", startString)
            .addQueryParameter("primary_release_date.lte", endString)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val resultsArray = response?.getJSONArray("results")
                    for (i in 0 until resultsArray!!.length()) {
                        val movieObj = resultsArray.getJSONObject(i)
                        val movie = Movie(movieObj)
                        movies.add(movie)
                    }
                    callback.onSuccess(movies)
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(application, anError?.message, Toast.LENGTH_SHORT).show()
                }
            })

    }

    fun getPopular(callback: GetPopularCallback) {
        val movies = arrayListOf<Movie>()

        AndroidNetworking.get(URL_DISCOVER)
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("page", "1")
            .addQueryParameter("sort_by", "popularity.desc")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val resultsArray = response?.getJSONArray("results")
                    for (i in 0 until resultsArray!!.length()) {
                        val movieObj = resultsArray.getJSONObject(i)
                        val movie = Movie(movieObj)
                        movies.add(movie)
                    }
                    callback.onSuccess(movies)
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(application, anError?.errorDetail, Toast.LENGTH_SHORT).show()
                }
            })

    }

    fun getMovieDetails(id: String, callback: GetMovieDetailsCallback) {
        AndroidNetworking.get("$URL_GET_MOVIE/$id")
            .addQueryParameter("api_key", API_KEY)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val movie = Movie(response!!)
                    movie.genre = response.getJSONArray("genres").getJSONObject(0).getString("name")
                    callback.onSuccess(movie)
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(application, anError?.errorDetail, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun getMovieCredits(movie: Movie, callback: GetMovieCreditsCallback) {
        AndroidNetworking.get("$URL_GET_MOVIE/${movie.id}/credits")
            .addQueryParameter("api_key", API_KEY)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val crewArray = response?.getJSONArray("crew")
                    val castArray = response?.getJSONArray("cast")
                    for (i in 0 until crewArray!!.length()) {
                        if (crewArray.getJSONObject(i).getString("job") == "Director") {
                            movie.director = crewArray.getJSONObject(i).getString("name")
                        }
                    }
                    val castsList = arrayListOf<Cast>()
                    for (i in 0 until castArray!!.length()) {
                        val castObj = castArray.getJSONObject(i)
                        val cast = Cast(castObj)
                        castsList.add(cast)
                    }
                    movie.casts = castsList
                    callback.onSuccess(movie)
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(application, anError?.errorDetail, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun getMovieTrailer(movie: Movie, callback: GetMovieTrailerCallback) {
        AndroidNetworking.get("$URL_GET_MOVIE/${movie.id}/videos")
            .addQueryParameter("api_key", API_KEY)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val resultsArray = response?.getJSONArray("results")
                    if (resultsArray!!.length() > 0){
                        movie.trailerVideoId = resultsArray.getJSONObject(0)!!.getString("key")
                        callback.onSuccess(movie)
                    }
                    else{
                        movie.trailerVideoId = "null"
                    }

                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(application, anError?.errorDetail, Toast.LENGTH_SHORT).show()
                }
            })
    }


    interface GetNowPlayingCallback {
        fun onSuccess(movies: List<Movie>)
    }

    interface GetPopularCallback {
        fun onSuccess(movies: List<Movie>)
    }

    interface GetMovieDetailsCallback {
        fun onSuccess(movie: Movie)
    }

    interface GetMovieCreditsCallback {
        fun onSuccess(movie: Movie)
    }

    interface GetMovieTrailerCallback {
        fun onSuccess(movie: Movie)
    }


}