package com.kbstar.movieproject.recyclerview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kbstar.movieproject.BookTicketActivity
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.MyPageActivity
import com.kbstar.movieproject.databinding.ItemBookingBinding
import com.kbstar.movieproject.dto.Booking
import retrofit2.Call
import retrofit2.Response

import java.text.SimpleDateFormat

class MyViewHolder(val binding: ItemBookingBinding): RecyclerView.ViewHolder(binding.root)

//datas - MainActivity 가 받은 항목을 구성하기 위한 서버데이터..
class BookingAdapter(val context: Context, val datas: MutableList<Booking>)
    : RecyclerView.Adapter<MyViewHolder>(){

    val updateLiveData = MutableLiveData<Booking>()
    val deleteLiveData = MutableLiveData<Booking>()

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemBookingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context)
            .load(datas[position].movie_thumbnail)
            .override(200, 200)
            .into(holder.binding.movieImageView)
        holder.binding.titleView.text = datas[position].movie_name
        holder.binding.theaterView.text = datas[position].theater_name
        holder.binding.dateView.text = datas[position].booking_date
        holder.binding.timeView.text = datas[position].booking_time

    }

    fun removeItem(index: Int){
        Log.d("kkang","removeItem : $index")
        datas.removeAt(index)
        //notifyDataSetChanged() 함수를 이용하면 전체 갱신되어 애니메이션 효과를 볼 수 없다.
        //notifyItemRemoved() 함수를 이용하면 아래 항목이 올라오듯이 삭제된다.
        notifyItemRemoved(index)
    }
    fun restoreItem(index: Int, data: Booking){
        datas.add(index, data)
        //애니메이션 효과에 의해 추가되며 아래 항목이 아래로 이동한다.`
        notifyItemInserted(index)
    }


    fun updateItem(index: Int, data: Booking){
        val intent = Intent(context.applicationContext, BookTicketActivity::class.java)
        intent.putExtra("mode", "update")
        intent.putExtra("bookingInfo", data)
        intent.putExtra("title", data.movie_name)
        intent.putExtra("booking_idx", data.booking_idx)
        Log.d("jbjung booking ====> ", data.toString())

//        val launcher = (context as MyPageActivity).registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ){
//            it.data
//        }
//
//        notifyDataSetChanged()
        context.startActivity(intent)
    }
}



