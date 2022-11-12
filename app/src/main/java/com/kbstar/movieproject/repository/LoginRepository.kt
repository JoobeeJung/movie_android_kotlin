package com.kbstar.movieproject.repository

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.dto.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginRepository(val application: MyApplication) {
    fun login(id: String, pw: String, callback: Callback<String>){
        val loginService = application.loginService
        val call = loginService.login(
            Users(user_idx = 0, user_id = id, user_pw=pw, user_name = "",
                Date(),"","" )
        )
        Log.d("jbjung222",call.toString())

        call.enqueue(callback)

//        call.enqueue(object: retrofit2.Callback<String>{
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                val result = response.body() ?:String
//                Log.d("jbjung",result.toString())
//
//                //preference로 로그인 성공후 아이디 저장해서 이후 로그인 상태 파악하겠다..
//                //아래의 이름은 저장소(파일명)
//                val preference = application.getSharedPreferences("userPrefs", AppCompatActivity.MODE_PRIVATE)
//                val editor = preference.edit()
//                editor.putString("loginId", id)
//                editor.commit()
//
//                //B/L 처리가 끝난후에.. View(Activity)에게 화면처리해라.. 명령..
//
//                resultStr = "ok"
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                t.printStackTrace()
//                call.cancel()
//                resultStr = "error"
//            }
//        })
//        return resultStr
    }
}