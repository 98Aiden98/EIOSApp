package com.example.eiosapp

import com.google.gson.annotations.SerializedName

data class Role(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Description")
    val description: String
)
