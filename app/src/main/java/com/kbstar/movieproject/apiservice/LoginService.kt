package com.kbstar.movieproject.apiservice

import com.kbstar.movieproject.dto.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("login.jbmovie")
    fun login(@Body users: Users): Call<String>

    @POST("logout.jbmovie")
    fun logout(@Body users:Users): Call<String>

}