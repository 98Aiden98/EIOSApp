package com.example.eiosapp.StudentRatingPlan

import com.example.eiosapp.TimeTablePackage.UserCrop
import com.google.gson.annotations.SerializedName

data class TestProfiles(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("TestTitle")
    val testTitle: String,
    @SerializedName("Creator")
    val creator: UserCrop
)
