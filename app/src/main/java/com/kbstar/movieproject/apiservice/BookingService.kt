package com.kbstar.movieproject.apiservice

import com.kbstar.movieproject.dto.Booking
import com.kbstar.movieproject.dto.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookingService {

    @GET("bookingList.jbmovie")
    fun getBooking(@Query("loginId") user_id:String?): Call<MutableList<Booking>>

    @POST("movieBooking.jbmovie")
    fun bookingMovie(@Query("mode") mode:String, @Body booking:Booking): Call<MutableList<Booking>>

    @GET("cancelTicket.jbmovie")
    fun cancelBooking(@Query("booking_idx") booking_idx:String, @Query("user_id") user_id:String?): Call<String>


}