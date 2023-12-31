package com.example.sus.activity.logic.auth.retrofit.dto

import com.example.eiosapp.UserInfo
import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("Admins")
    val admins: List<UserInfo>,

    @SerializedName("Party")
    val party: List<UserInfo>,

    @SerializedName("Id")
    val id: Int,

    @SerializedName("Наименование")
    val name: String,

    @SerializedName("ДатаНачало")
    val startDate: String,

    @SerializedName("ДатаОкончание")
    val endDate: String,

    @SerializedName("ВремяНачало")
    val startTime: String,

    @SerializedName("ВремяОкончание")
    val endTime: String,

    @SerializedName("Место")
    val location: String,

    @SerializedName("Ответственный")
    val responsible: String,

    @SerializedName("IsForAll")
    val isForAll: Boolean,

    @SerializedName("Описание")
    val description: String,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("LongEventMarker")
    val longEventMarker: Boolean,

    @SerializedName("AddTimeMarker")
    val addTimeMarker: Boolean,

    @SerializedName("ТехИнфо")
    val technicalInfo: String,

    @SerializedName("Подписка")
    val subscription: Boolean
)

