package com.kbstar.movieproject.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BoxOffice(
    var movie_idx: Int?,
    var movie_name: String,
    var movie_rank: Int?,
    var movie_thumbnail: String,
    var movie_status: Int?,
    var movie_IMDbId: String
): Parcelable
