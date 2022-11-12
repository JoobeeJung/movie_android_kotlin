package com.kbstar.movieproject

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kbstar.movieproject.databinding.DialogLayoutBinding
import com.kbstar.movieproject.dto.Booking
import retrofit2.Call
import retrofit2.Response

class CustomDialog(val title:String, val seat_num:String, val theater_name:String, val movie_date:String, val movie_time:String, val loginId:String?): DialogFragment() {
    private var _binding: DialogLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogLayoutBinding.inflate(inflater, container, false)
//        val view = binding.root

        binding.bookingView.text = "Title : ${title} \nTheater : ${theater_name}\nDate : ${movie_date}\nTime : ${movie_time}\nSeat Num : ${seat_num}"

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnYes.setOnClickListener {
            confirmBooking()
        }
        binding.btnNo.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun confirmBooking(){
        Log.d("MyTag", "positive")

        val bookingService = (activity?.applicationContext as MyApplication).bookingService

        val call = bookingService.bookingMovie(
            "",
            Booking(
                booking_idx="",
                movie_idx = "",
                movie_name = title,
                seat_num = seat_num,
                theater_name = theater_name,
                booking_date= movie_date,
                booking_time = movie_time,
                user_id = loginId,
                movie_thumbnail = "",
                movie_IMDbId = ""),
        )

        Log.d("jbjung_seatTicket",call.toString())
        call.enqueue(object: retrofit2.Callback<MutableList<Booking>>{
            override fun onResponse(
                call: Call<MutableList<Booking>>,
                response: Response<MutableList<Booking>>
            ) {
                val result = response.body() ?:String
                Log.d("jbjung booking =====> ",result.toString())
                //성공시 마이페이지로 전환

                val intent = Intent(activity, MyPageActivity::class.java)
                intent.putExtra("user_id", "${loginId}")
                startActivity(intent)

            }

            override fun onFailure(
                call: Call<MutableList<Booking>>,
                t: Throwable
            ) {

                t.printStackTrace()
                call.cancel()
            }
        })
    }
}