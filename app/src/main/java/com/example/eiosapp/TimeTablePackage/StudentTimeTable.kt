package com.example.eiosapp.TimeTablePackage

import com.google.gson.annotations.SerializedName

data class StudentTimeTable(
    @SerializedName("Group")
    val group: String,
    @SerializedName("PlanNumber")
    val planNumber: String,
    @SerializedName("FacultyName")
    val facultyName: String,
    @SerializedName("TimeTableBlockd")
    val timeTableBlockd: Int,
    @SerializedName("TimeTable")
    val timeTable: TimeTable
)


