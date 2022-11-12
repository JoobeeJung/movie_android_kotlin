package com.kbstar.movieproject.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

//서버 연동 데이터 추상화 .. vo, dto ...
//intent 의 extra data에 추가할 것이다.. Parcelable 구현해야함

@Parcelize
data class Users(
    var user_idx: Int,
    var user_id: String,
    var user_pw: String,
    var user_name: String,
    var user_birthDate: Date,
    var user_mobile:String,
    var user_email: String): Parcelable