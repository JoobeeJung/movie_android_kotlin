package com.kbstar.movieproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.kbstar.movieproject.databinding.ActivityMyPageBinding
import com.kbstar.movieproject.dto.Booking
import com.kbstar.movieproject.recyclerview.BookingAdapter
import com.kbstar.movieproject.swifemenu.step.SwipeDeleteCallback
import com.kbstar.movieproject.viewmodel.LoginViewModel
import com.kbstar.movieproject.viewmodel.MyPageViewModel
import retrofit2.Call
import retrofit2.Response


class MyPageActivity : AppCompatActivity() {
    var bookingList = mutableListOf<Booking>()//서버 데이터
    var adapter = BookingAdapter(this, bookingList)
    var mode = ""// update의 경우 수정

    lateinit var loginId: String

    val viewModel: MyPageViewModel by viewModels<MyPageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val preference = getSharedPreferences("userPrefs", MODE_PRIVATE)
        loginId = preference.getString("loginId", "") ?: ""

        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //gridlayoutmanager로 변경
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeDeleteCallback(this, binding.recyclerView, bookingList))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)


        val dividerDecoration = DividerItemDecoration(
            this, LinearLayoutManager(this).orientation
        )
        dividerDecoration.setDrawable(resources.getDrawable(R.drawable.divider))
        binding.recyclerView.addItemDecoration(dividerDecoration)

        viewModel.getServerData(loginId)

        viewModel.myPageLiveData.observe(this){
            bookingList.clear()
            bookingList.addAll(it) //없으면 빈것
            adapter.notifyDataSetChanged()//갱신
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getServerData(loginId)
    }

    override fun onBackPressed() {
//        super.onBackPressed()

        //유저 백버튼 이벤트 처리....
        val intent = Intent(this, MainActivity::class.java)
        //history(이전 액티비티) 날리고.. 새로 쌓아라..
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}