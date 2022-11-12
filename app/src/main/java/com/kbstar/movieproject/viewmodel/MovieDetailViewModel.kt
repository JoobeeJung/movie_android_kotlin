package com.kbstar.movieproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.dto.Booking
import com.kbstar.movieproject.dto.Movie
import com.kbstar.movieproject.repository.LoginRepository
import com.kbstar.movieproject.repository.MovieDetailRepository
import retrofit2.Call
import retrofit2.Response


//MVVM (Model - View - ViewModel)
//View : UI 처리...
//Model : B/L 처리..
//ViewModel.... View와 Model 연결..

//ViewModel 에 B/L 을 담지 않고.. B/L 처리하는 클래스를 따로 두는 것이 일반적...
//일반적으로 안드로이드의 경우 B/L 이라는 ㄷ것이 데이터 저장, 획득등이며 이를 흔히 Repository 라고 부른다..
//그럼으로.. 서버 네트워킹... dbms 등은 Repository 에 작성..
class MovieDetailViewModel(application: Application): AndroidViewModel(application) {

    val movieDetailLiveData = MutableLiveData<Movie?>()

    fun getMovieDetail(id: String?){
        Log.d("jbjung","getMovieDetail")

        val myApplication = getApplication<MyApplication>()
        val repository = MovieDetailRepository(myApplication)
        val callback = object: retrofit2.Callback<Movie>{
            override fun onResponse(
                call: Call<Movie>,
                response: Response<Movie>
            ) {
                val resultData = response.body()!!
                Log.d("jbjung","mypage viewmodel : ${resultData}")
                movieDetailLiveData.postValue(resultData)

            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
                movieDetailLiveData.postValue(null)
            }
        }

        val resultData = repository.getMovieDetail(id, callback)

        //ViewModel 과 Repository 를 분리시키는 것이 의미없어 보일지 모르겠으나..
        //실전 프로젝트에서는 View(Activity), B/L(Repository) 이외에 추가 처리할 내용들이 꽤 된다.. 이를 ViewModel 이 담당

    }

}