package com.example.eiosapp.TokenPackage

import com.google.gson.annotations.SerializedName

data class AuthRequest(
   @SerializedName("grant_type")
   val grantType: String = "password",
   @SerializedName("username")
   val username: String,
   @SerializedName("password")
   val password: String,
   @SerializedName("client_id")
   val clientId: String = "8",
   @SerializedName("client_secret")
   val clientSecret: String = "qweasd"
)
