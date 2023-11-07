package com.example.eiosapp.TimeTablePackage

import com.google.gson.annotations.SerializedName

data class TimeTableLessonDiscipline(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Language")
    val language: String,
    @SerializedName("LessonType")
    val lessonType: TimeTableLessonDisciplineType,
    @SerializedName("Remote")
    val remote: Boolean,
    @SerializedName("Group")
    val group: String,
    @SerializedName("SubgroupNumber")
    val subgroupNumber: Byte,
    @SerializedName("Teacher")
    val teacher: UserCrop,
    @SerializedName("Auditorium")
    val auditorium: Auditorium
)
