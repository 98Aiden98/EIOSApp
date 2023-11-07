package com.example.eiosapp.TimeTablePackage

import com.example.eiosapp.UserPackage.UserPhoto
import com.google.gson.annotations.SerializedName

data class UserCrop(
    @SerializedName("Id")
    val id: String,
    @SerializedName("UserName")
    val name: String,
    @SerializedName("FIO")
    val fio: String,
    @SerializedName("Photo")
    val photo: UserPhoto
)
