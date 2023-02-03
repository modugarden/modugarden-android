package com.example.modugarden.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapInfo(
    val location : String,
    val address : String,
    val lat : Double,
    val lng : Double,
):Parcelable
