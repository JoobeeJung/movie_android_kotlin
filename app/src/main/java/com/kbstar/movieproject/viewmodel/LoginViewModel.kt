package com.kbstar.movieproject.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.repository.LoginRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//MVVM (Model - View - ViewModel)
//View : UI 처리...
//Model : B/L 처리..
//ViewModel.... View와 Model 연결..

//ViewModel 에 B/L 을 담지 않고.. B/L 처리하는 클래스를 따로 두는 것이 일반적...
//일반적으로 안드로이드의 경우 B/L 이라는 ㄷ것이 데이터 저장, 획득등이며 이를 흔히 Repository 라고 부른다..
//그럼으로.. 서버 네트워킹... dbms 등은 Repository 에 작성..
class LoginViewModel(application: Application): AndroidViewModel(application) {

    val loginLiveData = MutableLiveData<String>()

    fun login(id: String, pw: String){
        Log.d("kkang","111111")
        val myApplication = getApplication<MyApplication>()

        val repository = LoginRepository(myApplication)

        //ViewModel 과 Repository 를 분리시키는 것이 의미없어 보일지 모르겠으나..
        //실전 프로젝트에서는 View(Activity), B/L(Repository) 이외에 추가 처리할 내용들이 꽤 된다.. 이를 ViewModel 이 담당
        repository.login(id, pw, object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {

                val result = response.body()!!
                Log.d("jbjung","1111111${result}")

                //preference로 로그인 성공후 아이디 저장해서 이후 로그인 상태 파악하겠다..
                //아래의 이름은 저장소(파일명)
                val preference = myApplication.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
                val editor = preference.edit()
                editor.putString("loginId", id)
                editor.commit()

                //B/L 처리가 끝난후에.. View(Activity)에게 화면처리해라.. 명령..

                loginLiveData.postValue("ok")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                loginLiveData.postValue("error")
            }
        })

    }

}