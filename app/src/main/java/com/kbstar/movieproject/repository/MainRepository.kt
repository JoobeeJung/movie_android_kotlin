package com.kbstar.movieproject.repository

import android.util.Log
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.dto.BoxOffice
import com.kbstar.movieproject.dto.Movie
import com.kbstar.movieproject.dto.MovieResult

class MainRepository(val application: MyApplication) {
    fun getBoxoffice(callback: retrofit2.Callback<MutableList<BoxOffice>>){
        Log.d("jbjung","getBoxoffice")
        //영화 목록 DB 서버에서
        val boxOfficeService =application.boxOfficeService
        val call = boxOfficeService.getBoxoffice()
        call.enqueue(callback)
    }

    fun getSearchResult(keyword:String, callback: retrofit2.Callback<MovieResult>){
        Log.d("jbjung","getSearchResult")
        //영화 검색 목록 api 호출

        val apiKey = "k_t7zmd184"//"k_1bwxl3vd" //k_t7zmd184
        val url = "https://imdb-api.com/en/API/Search/$apiKey/$keyword"

        val movieSearchService =application.movieSearchService
        val call = movieSearchService.searchMovie(url)
        call.enqueue(callback)
    }
}