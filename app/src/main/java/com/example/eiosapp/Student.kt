package com.example.eiosapp

import com.google.gson.annotations.SerializedName

data class Student (
    @SerializedName("Id")
    val id: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("EmailConfirmed")
    val emailConfirmed: Boolean,
    @SerializedName("FIO")
    val fio: String,
    @SerializedName("EnglishFIO")
    val englishFIO: String,
    @SerializedName("StudentCod")
    val studentCod: String,
    @SerializedName("BirthDate")
    val birthDate: String,
    @SerializedName("SNILS")
    val snils: String,
    @SerializedName("Foreigner")
    val foreigner: Boolean,
    @SerializedName("Citizenship")
    val citizenship: String,
    @SerializedName("Sex")
    val sex: String,
    @SerializedName("Address")
    val address: String,
    @SerializedName("Contacts")
    val contact: String,
    @SerializedName("RecordBooks")
    val recordBooks: List<RecordBooks>
)
