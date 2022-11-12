package com.kbstar.movieproject.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Theater(
    var theater_idx: Int,
    var theater_name: String,
    var theater_address: String,
    var theater_latitude: String,
    var theater_longtitude: String,
    var theater_tel: String,
    ): Parcelable
