package com.kbstar.movieproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import com.kbstar.movieproject.databinding.ActivitySeatTicketBinding
import com.kbstar.movieproject.dto.Booking
import java.net.URLEncoder



class SeatTicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySeatTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preference = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val loginId = preference.getString("loginId", null)

        loginId?.run {
            Log.d("kkang","login ok... $loginId")
        } ?: let {
            Log.d("kkang","logout....")
        }

        val bookingInfo: Booking = intent.getParcelableExtra<Booking>("bookingInfo")!!
        val mode = intent.getStringExtra("mode").toString()
        var image = intent.getStringExtra("image").toString()
        val title = URLEncoder.encode(bookingInfo.movie_name,"utf-8")
        val movie_date = URLEncoder.encode(bookingInfo.booking_date,"utf-8")
        val theater_name = URLEncoder.encode(bookingInfo.theater_name,"utf-8")
        val movie_time = URLEncoder.encode(bookingInfo.booking_time,"utf-8")
        image = URLEncoder.encode(image, "utf-8")

        lateinit var requestUrl: String;

        Log.d("jbjung seatTicket", mode)

        requestUrl = "http://10.10.220.79:7777/movieSeat.jbmovie?title=$title&image=$image&movie_date=$movie_date&theater_name=$theater_name&movie_time=$movie_time"

        Log.d("jbjung url", requestUrl)

        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        val webChromeClient = object : WebChromeClient(){
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                //js alert의 문자열을 toast로 띄우고..
                //js alert 닫아버린다
                Toast.makeText(this@SeatTicketActivity, message, Toast.LENGTH_SHORT).show()
                result?.confirm()
                return true
            }
        }

        class JavascriptTest {
            @JavascriptInterface
            fun moveCheckout(title:String, theater_name:String, movie_date:String, movie_time:String, seat_num:String){
                Log.d("kkang","moveCheckout.............${title},${theater_name},${movie_date},${movie_time},${seat_num}")

                showMessageDialog(title, seat_num, theater_name, movie_date, movie_time, loginId)
            }
        }

        binding.webView.run {

            //browser 이벤트 등록

            setWebChromeClient(webChromeClient)
            addJavascriptInterface(JavascriptTest(), "android")

            //초기 html 로딩..
            loadUrl(requestUrl)
        }
    }

    private fun showMessageDialog(title:String, seat_num:String, theater_name:String, movie_date:String,movie_time:String, loginId:String?){
        //ㄷㅏ이얼로그 본문.. 준비..
        val customDialog = CustomDialog(title, seat_num, theater_name, movie_date, movie_time, loginId)

        customDialog.show(supportFragmentManager, "CustomDialog")
    }


}