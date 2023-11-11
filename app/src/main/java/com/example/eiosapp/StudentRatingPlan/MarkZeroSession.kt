package com.example.eiosapp.StudentRatingPlan

import com.google.gson.annotations.SerializedName

data class MarkZeroSession(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Ball")
    val ball: Double,
    @SerializedName("CreatorId")
    val creatorId: String,
    @SerializedName("CreateDate")
    val createDate: String
)
