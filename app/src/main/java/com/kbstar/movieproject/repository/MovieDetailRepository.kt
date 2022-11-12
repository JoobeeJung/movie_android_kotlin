package com.kbstar.movieproject.repository

import android.util.Log
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.dto.Movie


class MovieDetailRepository(val application: MyApplication) {
    fun getMovieDetail(id: String?, callback: retrofit2.Callback<Movie>){
        Log.d("jbjung","getMovieDetail")
        val apiKey = "k_t7zmd184"//"k_1bwxl3vd"
        val url = "https://imdb-api.com/en/API/Title/$apiKey/$id"

        //영화 detail api 서버에서
        val movieService =application.movieService
        val call = movieService.getMovieDetail(url)
        call.enqueue(callback)
        }

    }