package com.example.eiosapp.TimeTablePackage

import com.google.gson.annotations.SerializedName

data class TimeTableLesson(
    @SerializedName("Number")
    val number: Byte,
    @SerializedName("SubgroupCount")
    val subgroupCount: Byte,
    @SerializedName("Disciplines")
    val disciplines: List<TimeTableLessonDiscipline>
)
