package com.kbstar.movieproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.dto.Booking
import com.kbstar.movieproject.dto.BoxOffice
import com.kbstar.movieproject.dto.Movie
import com.kbstar.movieproject.dto.MovieResult
import com.kbstar.movieproject.repository.LoginRepository
import com.kbstar.movieproject.repository.MainRepository
import com.kbstar.movieproject.repository.MovieDetailRepository
import retrofit2.Call
import retrofit2.Response

class MainViewModel(application: Application): AndroidViewModel(application){

    val boxOfficeLiveData = MutableLiveData<MutableList<BoxOffice>>()
    val movieSearchedLiveData =MutableLiveData<MovieResult?>()

    fun getBoxoffice() {
        Log.d("jbjung", "getBoxoffice")

        val myApplication = getApplication<MyApplication>()
        val repository = MainRepository(myApplication)

        val callback = object: retrofit2.Callback<MutableList<BoxOffice>>{
            override fun onResponse(
                call: Call<MutableList<BoxOffice>>,
                response: Response<MutableList<BoxOffice>>
            ) {
                val resultData = response.body() ?: mutableListOf()
                Log.d("jbjung","boxoffice viewmodel : ${resultData.size}")
                boxOfficeLiveData.postValue(resultData)
            }

            override fun onFailure(call: Call<MutableList<BoxOffice>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                boxOfficeLiveData.postValue(mutableListOf())

            }
        }
        val resultData = repository.getBoxoffice(callback)
    }
    fun getSearchResult(keyword: String) {
        Log.d("jbjung", "getBoxoffice")

        val myApplication = getApplication<MyApplication>()
        val repository = MainRepository(myApplication)

        val callback = object: retrofit2.Callback<MovieResult>{
            override fun onResponse(
                call: Call<MovieResult>,
                response: Response<MovieResult>
            ) {
                val movie_searched = response.body()!!
                Log.d("jbjung","movie_searched viewmodel : ${movie_searched}")
                movieSearchedLiveData.postValue(movie_searched)
            }

            override fun onFailure(call: Call<MovieResult>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                movieSearchedLiveData.postValue(null)

            }
        }
        val resultData = repository.getSearchResult(keyword, callback)
    }
}