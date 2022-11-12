package com.kbstar.movieproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.kbstar.movieproject.databinding.ActivityMovieDetailBinding
import com.kbstar.movieproject.dto.Movie
import com.kbstar.movieproject.viewmodel.MovieDetailViewModel
import com.kbstar.movieproject.viewmodel.MyPageViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieDetailBinding
    lateinit var image: String
    val viewModel: MovieDetailViewModel by viewModels<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        val title: String = intent.getStringExtra("title").toString()
        val from: String = intent.getStringExtra("from").toString()
        image = intent.getStringExtra("image").toString()

        if(from != "null")
        {
            Log.d("jbjung from", "not null !!!")
            binding.btnBooking.visibility = View.VISIBLE


        }else
        {
            Log.d("jbjung from", "null !!!")
            binding.btnBooking.visibility = View.GONE
        }

        viewModel.getMovieDetail(id)

        viewModel.movieDetailLiveData.observe(this){

            it?.let {
                Glide.with(this@MovieDetailActivity)
                    .load(it.image)
                    .override(1000, 1000)
                    .thumbnail(Glide.with(this@MovieDetailActivity).load(R.drawable.loading_unscreen))
                    .into(binding.itemImageView)

                binding.itemTitleTextView.text = it.title
                binding.itemRelaseDateTextView.text = it.releaseDate
                binding.itemRuntimeStrTextView.text = it.runtimeStr
                binding.itemPlotTextView.text = it.plot
                binding.itemDirectorsTextView.text = it.directors
                binding.itemWritersTextView.text = it.writers
                binding.itemStarsTextView.text = it.stars
                binding.itemCountriesTextView.text = it.countries
                binding.itemGenresTextView.text = it.genres
                binding.itemKeywordsTextView.text = it.keywords

                SystemClock.sleep(200)

                binding.parentShimmerLayout.visibility = View.GONE
                binding.parentShimmerLayout.stopShimmer()
                binding.detailMainContent.visibility = View.VISIBLE
            } ?: let {
                Toast.makeText(this, "영화 상세 가져오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnBooking.setOnClickListener { bookTicket(title) }
    }
    fun bookTicket(title: String)
    {
        val intent = Intent(applicationContext, BookTicketActivity::class.java)
        intent.putExtra("title", "${title}")
        intent.putExtra("image", "${image}")

        startActivity(intent)
    }
}