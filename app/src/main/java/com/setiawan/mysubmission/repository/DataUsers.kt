package com.setiawan.mysubmission.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataUsers(
    var name: String? = "",
    var username: String? = "",
    var avatar: String? = "",
    var company: String? = "",
    var location: String? = "",
    var repository: String? = "",
    var followers: String? = "",
    var following: String? = "",
    var bio : String? = ""
) : Parcelable