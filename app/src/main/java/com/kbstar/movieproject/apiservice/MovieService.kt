package com.kbstar.movieproject.apiservice

import com.kbstar.movieproject.dto.BoxOffice
import com.kbstar.movieproject.dto.Movie
import com.kbstar.movieproject.dto.MovieResult
import com.kbstar.movieproject.dto.MovieSearched
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface MovieService {
    @GET
    fun getMovieDetail(@Url url: String): Call<Movie>

    @GET
    fun searchMovie(@Url url: String): Call<MovieResult>
}