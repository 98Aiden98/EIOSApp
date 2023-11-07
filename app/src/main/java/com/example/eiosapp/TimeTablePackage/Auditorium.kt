package com.example.eiosapp.TimeTablePackage

import com.google.gson.annotations.SerializedName

data class Auditorium(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Number")
    val number: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("CampusId")
    val campusid: Int,
    @SerializedName("CampusTitle")
    val campustitle: String
)
