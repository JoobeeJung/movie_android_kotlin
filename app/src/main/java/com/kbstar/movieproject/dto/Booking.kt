package com.kbstar.movieproject.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    val booking_idx: String,
    val movie_idx: String,
    val movie_name: String,
    val seat_num: String,
    val theater_name: String,
    val booking_date: String,
    val booking_time: String,
    val user_id: String?,
    val movie_thumbnail: String,
    val movie_IMDbId: String,
): Parcelable
