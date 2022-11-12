package com.kbstar.movieproject.apiservice

import com.kbstar.movieproject.dto.BoxOffice
import com.kbstar.movieproject.dto.Theater
import retrofit2.Call
import retrofit2.http.GET

interface BoxOfficeService {

    @GET("boxoffice.jbmovie")
    fun getBoxoffice(): Call<MutableList<BoxOffice>>

    @GET("theater.jbmovie")
    fun getTheater(): Call<MutableList<Theater>>
}