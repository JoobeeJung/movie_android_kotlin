package com.kbstar.movieproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.kbstar.movieproject.databinding.ActivitySearchBinding
import com.kbstar.movieproject.dto.MovieResult
import com.kbstar.movieproject.dto.MovieSearched
import com.kbstar.movieproject.recyclerview.SearchAdapter
import retrofit2.Call
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    var movieList = mutableListOf<MovieSearched>()
    val adapter = SearchAdapter(this, movieList)

    lateinit var binding: ActivitySearchBinding
    lateinit var search_keyword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.searchRecyclerView.adapter = adapter

        binding.btnSearch.setOnClickListener{
            searchMovie()

            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }

    }

    fun searchMovie() {
        search_keyword = binding.inputText.text.toString()

        Log.d("jbjung::search",search_keyword)

        val movieSearchService = (applicationContext as MyApplication).movieService
        val apiKey = "k_1bwxl3vd" //k_t7zmd184
        val call = movieSearchService.searchMovie("https://imdb-api.com/en/API/Search/$apiKey/$search_keyword")

        call.enqueue(object : retrofit2.Callback<MovieResult> {
            override fun onResponse(
                call: Call<MovieResult>,
                response: Response<MovieResult>
            ) {
                val movie_searched = response.body()!!
                Log.d("jbjung::Search_API", "${movie_searched.results}")
                movieList.clear()
                movieList.addAll(response.body()!!.results)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MovieResult>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })
    }
}