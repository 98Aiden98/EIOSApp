package com.example.eiosapp.StudentRatingPlan

import com.google.gson.annotations.SerializedName

enum class OldRatingPlanSectionType {
    @SerializedName("Экзамен")
    EXAM,
    @SerializedName("Текущий")
    DEFAULT,
    @SerializedName("Курсовая")
    PROJECT
}