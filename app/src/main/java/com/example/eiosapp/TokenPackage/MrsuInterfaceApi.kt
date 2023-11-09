package com.example.eiosapp.TokenPackage

import com.example.eiosapp.StudentPackage.Student
import com.example.eiosapp.StudentSemesterPackage.StudentSemester
import com.example.eiosapp.TimeTablePackage.StudentTimeTable
import com.example.eiosapp.UserPackage.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MrsuInterfaceApi {
    @GET("v1/User")
    suspend fun getUser(@Header("Authorization") authorization: String): User

    @GET("v1/StudentInfo")
    suspend fun getStudent(@Header("Authorization") authorization: String): Student

    @GET("v1/StudentSemester?selector=current")
    suspend fun getStudentSemester(@Header ("Authorization") authorization: String): StudentSemester

    @GET ("v1/StudentTimeTable")
    suspend fun getStudentTimeTable(
        @Header ("Authorization") authorization: String,
        @Query("date") date: String)
    : List<StudentTimeTable>

    @FormUrlEncoded
    @POST("OAuth/Token")
    suspend fun getToken(
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("client_id") clientId: String = "8",
        @Field("client_secret") clientSecret: String = "qweasd"
    ): Token

    @FormUrlEncoded
    @POST("OAuth/Token")
    suspend fun getNewToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String = "8",
        @Field("client_secret") clientSecret: String = "qweasd"
    ): Token
}
