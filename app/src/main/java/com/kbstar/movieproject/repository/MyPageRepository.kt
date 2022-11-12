package com.kbstar.movieproject.repository

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.dto.Booking
import com.kbstar.movieproject.dto.Users
import retrofit2.Call
import retrofit2.Response
import java.util.*

class MyPageRepository(val application: MyApplication) {
    fun getServerData(loginId: String, callback: retrofit2.Callback<MutableList<Booking>>) {
        Log.d("jbjung","getServerData")
        var resultData = mutableListOf<Booking>()

        //목록 데이터 서버에서
        val bookingService =application.bookingService
        val call = bookingService.getBooking(loginId)
        call.enqueue(callback)
    }
}