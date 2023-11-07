package com.example.eiosapp.TimeTablePackage

import com.google.gson.annotations.SerializedName

data class TimeTable(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Lessons")
    val lessons: List<TimeTableLesson>
)
