package com.example.modugarden.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportInfo (
    var modalType : Int,
    var modalTitle : String
):Parcelable