package com.example.eiosapp.TokenPackage

import com.example.eiosapp.MessagerPackage.ForumMessage
import com.example.eiosapp.StudentPackage.Student
import com.example.eiosapp.StudentRatingPlan.StudentRatingPlan
import com.example.eiosapp.StudentSemesterPackage.StudentSemester
import com.example.eiosapp.TimeTablePackage.StudentTimeTable
import com.example.eiosapp.UserPackage.User
import com.example.sus.activity.logic.auth.retrofit.dto.Event
import com.example.sus.activity.logic.auth.retrofit.dto.EventInfo
import retrofit2.http.DELETE
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

    @GET("v1/StudentRatingPlan")
    suspend fun getStudentRatingPlan(
        @Header ("Authorization") authorization: String,
        @Query ("id") id: Int)
    : StudentRatingPlan

    @GET("v1/ForumMessage")
    suspend fun getForumMessage(
        @Header ("Authorization") authorization: String,
        @Query ("disciplineId") id: Int)
            : List<ForumMessage>

    @GET("v1/Event")
    suspend fun getEventById(
        @Header("Authorization") authorization: String,
        @Query("eventid") eventId: String
    ): Event

    @GET("v1/Events")
    suspend fun getEvents(
        @Header("Authorization") authorization: String,
    ): List<EventInfo>

    @GET ("v1/StudentTimeTable")
    suspend fun getStudentTimeTable(
        @Header ("Authorization") authorization: String,
        @Query("date") date: String)
    : List<StudentTimeTable>

    @DELETE("v1/ForumMessage")
    suspend fun deleteForumMessage(
        @Header ("Authorization") authorization: String,
        @Query ("id") id: Int
    ): Unit

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
    @POST("v1/ForumMessage")
    suspend fun sendForumMessage(
        @Header ("Authorization") authorization: String,
        @Query ("disciplineId") id: Int,
        @Field ("Text") text: String)
            : ForumMessage

    @FormUrlEncoded
    @POST("OAuth/Token")
    suspend fun getNewToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String = "8",
        @Field("client_secret") clientSecret: String = "qweasd"
    ): Token
}
