package com.kbstar.movieproject


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kbstar.movieproject.databinding.ActivityBookTicketBinding
import com.kbstar.movieproject.dto.Booking
import com.kbstar.movieproject.dto.Theater
import retrofit2.Call
import retrofit2.Response
import java.util.*


class BookTicketActivity : AppCompatActivity() {
    var dateString = ""
    var theaterList = mutableListOf<Theater>()
    lateinit var binding: ActivityBookTicketBinding;
    lateinit var nameList: MutableList<String>
    lateinit var title: String
    lateinit var mode: String
    lateinit var image: String
    var ticket_num:Int = 0

    lateinit var preference: SharedPreferences
    lateinit var loginId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preference = getSharedPreferences("userPrefs", MODE_PRIVATE)
        loginId = preference.getString("loginId", null) ?: ""

        binding = ActivityBookTicketBinding.inflate(layoutInflater)
        title = intent.getStringExtra("title").toString()
        mode = intent.getStringExtra("mode").toString()
        image = intent.getStringExtra("image").toString()

        setContentView(binding.root)

        binding.titleTextView.text = title

        binding.selectedDate.setOnClickListener {
            setupDatePicker()
        }

        setupSpinner()
        initNumberPicker()

        getBoxoffice()

        binding.btnDialog.setOnClickListener {
            setupListDialog()
        }


        //data 확인 테스트............
        binding.btnBook.setOnClickListener {
            Log.d("jbjung mode",mode)

            if(mode == "update")
            {
                Log.d("jbjung book",mode)
                val bookingService = (applicationContext as MyApplication).bookingService
                val call = bookingService.bookingMovie(
                    mode,
                    Booking(
                        movie_name = title,
                        booking_date = dateString,
                        booking_time = binding.timeSpinner.selectedItem.toString(),
                        theater_name = binding.selectedTheater.text.toString(),
                        booking_idx= intent.getStringExtra("booking_idx").toString(),
                        movie_idx = "",
                        seat_num = "",
                        user_id = loginId,
                        movie_thumbnail = "",
                        movie_IMDbId = ""),
                )

                Log.d("jbjung update =>","$title : $dateString ${binding.timeSpinner.selectedItem.toString()} : ${binding.selectedTheater.text.toString()}")
                call.enqueue(object: retrofit2.Callback<MutableList<Booking>>{
                    override fun onResponse(
                        call: Call<MutableList<Booking>>,
                        response: Response<MutableList<Booking>>
                    ) {
                        val result = response.body() ?:String
                        Log.d("jbjung booking =====> ",result.toString())
                        //성공시 마이페이지로 전환
                        finish()
                    }

                    override fun onFailure(
                        call: Call<MutableList<Booking>>,
                        t: Throwable
                    ) {

                        t.printStackTrace()
                        call.cancel()
                    }
                })

            }else
            {
                Log.d("jbjung update",mode)
                gotoBook();

            }
        }
    }

    fun gotoBook() {
        val intent = Intent(applicationContext, SeatTicketActivity::class.java)

        val bookingInfo = Booking(
            booking_idx = "",
            movie_name = title,
            booking_date = dateString,
            booking_time = binding.timeSpinner.selectedItem.toString(),
            theater_name = binding.selectedTheater.text.toString(),
            movie_idx = "",
            seat_num = "",
            user_id = "",
            movie_thumbnail = "",
            movie_IMDbId = ""
        )

        Log.d("kkang","booking:$bookingInfo")
        intent.putExtra("mode", mode)
        intent.putExtra("bookingInfo", bookingInfo)
        intent.putExtra("image",image)
        startActivity(intent)
    }

    fun getBoxoffice(){
        val boxOfficeService = (applicationContext as MyApplication).boxOfficeService
        val call = boxOfficeService.getTheater()
        call.enqueue(object: retrofit2.Callback<MutableList<Theater>>{
            override fun onResponse(
                call: Call<MutableList<Theater>>,
                response: Response<MutableList<Theater>>
            ) {
                theaterList.addAll(response.body() ?: mutableListOf())

                nameList = mutableListOf<String>()
                theaterList.forEach {
                    nameList.add(it.theater_name)
                }
                Log.d("jbjung_Theater", theaterList.toString())
            }

            override fun onFailure(call: Call<MutableList<Theater>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun setupSpinner() {
        val timeList  = arrayOf("09:30", "12:00", "15:00", "17:00", "19:30", "22:00")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.timeSpinner.adapter = arrayAdapter
    }

    private fun setupDatePicker(){
        val cal = Calendar.getInstance()    //캘린더뷰 만들기

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}-${month+1}-${dayOfMonth}"
            binding.selectedDate.text = "Date : "+dateString
        }
        val dateDialog = DatePickerDialog(this, R.style.MyDatePickerStyle, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
        dateDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dateDialog.show()
    }

    private fun setupListDialog(){
        val mBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialog)
        mBuilder.setTitle("Choose a theater")
        mBuilder.setSingleChoiceItems(nameList.toTypedArray(), -1) { dialogInterface, i ->
            binding.selectedTheater.text = nameList.toTypedArray()[i]
            dialogInterface.dismiss()
            val latitude = theaterList[i].theater_latitude
            val longitude = theaterList[i].theater_longtitude
            val theater_name = theaterList[i].theater_name
            val theater_tel = theaterList[i].theater_tel

            Log.d("jbjung_theater", "${theater_name} | ${theater_tel}")

            binding.btnMap.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${latitude},${longitude}?q=${latitude},${longitude}(${theater_name})"))
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
            binding.btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${theater_tel}"))
                startActivity(intent)
            }
        }
        // Set the neutral/cancel button click listener
        mBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Do something when click the neutral button
            dialog.cancel()
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }

    // 넘버 픽커 초기화
    fun initNumberPicker(){
        Log.d("ticketNum", binding.ticketNum.text.toString())

        binding.btnMinus.setOnClickListener {
            if(binding.ticketNum.text.toString() == "0")
            {
                Toast.makeText(this, "최소 구매 티켓 수량은 1장입니다.", Toast.LENGTH_SHORT).show()
            }else
            {
                ticket_num--
                binding.ticketNum.text = ticket_num.toString()
            }
        }
        binding.btnPlus.setOnClickListener {
            ticket_num++
            binding.ticketNum.text = ticket_num.toString()
        }



    }
}