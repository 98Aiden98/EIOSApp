package com.example.eiosapp.TokenPackage
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.eiosapp.StudentPackage.Student
import com.example.eiosapp.StudentRatingPlan.StudentRatingPlan
import com.example.eiosapp.StudentSemesterPackage.StudentSemester
import com.example.eiosapp.TimeTablePackage.StudentTimeTable
import com.example.eiosapp.UserPackage.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SharedPrefManager {

    //Ключи
    private const val PREF_NAME = "MyPrefs"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val STUDENT_DATA = "student_data"
    private const val USER_DATA = "user_data"
    private const val STUDENT_SEMESTER = "student_semester"
    private const val STUDENT_TIMETABLE = "student_timetable"
    private const val STUDENT_RATINGPLAN = "student_ratingplan"
    private const val EXPIRATION_TIME = "expiration_time"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var instance: SharedPrefManager

    //Получение экземпляра
    fun getInstance(context: Context): SharedPrefManager {
        if (!this::instance.isInitialized) {
            instance = SharedPrefManager
            init(context)
        }
        return instance
    }

    //Инициализация
    private fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val refreshToken = REFRESH_TOKEN
        if (refreshToken != null) {
            refreshDataUsingRefreshToken()
        }
    }

    //Обновление информации с помощью RefreshToken
    fun refreshDataUsingRefreshToken() {
        val BASE_URL_USER = "https://papi.mrsu.ru"

        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                //Проверка истечения времени использования токена
                checkTokenExpiration()

                //Получение токена
                val currentAccessToken = getAccessToken()

                //Обновление данных пользователя
                val refreshedUserData = userApi.getUser("Bearer ${currentAccessToken}")
                saveUserData(refreshedUserData)

                //Обновление данных студента
                val refreshedStudentData = userApi.getStudent("Bearer ${currentAccessToken}")
                saveStudentData(refreshedStudentData)

                //Обновление расписания студента
                val studentTimeTable = userApi.getStudentTimeTable("Bearer ${currentAccessToken}", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                saveStudentTimeTable(studentTimeTable)

            } catch (e: Exception) {

            }
        }

    }

    fun saveToken(userToken: Token) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, userToken.accessToken)
            putString(REFRESH_TOKEN, userToken.refreshToken)
            putLong(EXPIRATION_TIME, userToken.expiresIn * 1000 + System.currentTimeMillis() + 150)
            apply()
        }
    }

    fun saveUserData(userData: User) {
        val jsonUserData = Gson().toJson(userData)
        sharedPreferences.edit().apply {
            putString(USER_DATA, jsonUserData)
            apply()
        }
    }

    fun saveStudentSemester(userData: StudentSemester) {
        val jsonUserData = Gson().toJson(userData)
        sharedPreferences.edit().apply {
            putString(STUDENT_SEMESTER, jsonUserData)
            apply()
        }
    }

    fun saveStudentTimeTable(studentTimeTable: List<StudentTimeTable>) {
        val jsonStudentTimeTable = Gson().toJson(studentTimeTable)
        sharedPreferences.edit().apply {
            putString(STUDENT_TIMETABLE, jsonStudentTimeTable)
            apply()
        }
    }

    fun saveStudentRatingPlan(studentRatingPlan: StudentRatingPlan) {
        val jsonStudentTimeTable = Gson().toJson(studentRatingPlan)
        sharedPreferences.edit().apply {
            putString(STUDENT_RATINGPLAN, jsonStudentTimeTable)
            apply()
        }
    }

    fun getUserData(): User? {
        val jsonUserData = sharedPreferences.getString(USER_DATA, null)
        return Gson().fromJson(jsonUserData, User::class.java)
    }

    fun getStudentRatingPlan(param: (Any) -> Unit): StudentRatingPlan {
        val jsonStudentSemester = sharedPreferences.getString(STUDENT_RATINGPLAN, null)
        return Gson().fromJson(jsonStudentSemester, StudentRatingPlan::class.java)
    }

    fun getStudentSemester(): StudentSemester? {
        val jsonStudentSemester = sharedPreferences.getString(STUDENT_SEMESTER, null)
        return Gson().fromJson(jsonStudentSemester, StudentSemester::class.java)
    }

    fun getStudentTimeTable(): List<StudentTimeTable>? {
        val jsonStudentTimeTable = sharedPreferences.getString(STUDENT_TIMETABLE, null)
        val type: Type = object : TypeToken<List<StudentTimeTable>>() {}.type
        return Gson().fromJson(jsonStudentTimeTable, type)
    }

    fun saveStudentData(studentData: Student) {
        val jsonStudentData = Gson().toJson(studentData)
        sharedPreferences.edit().apply {
            putString(STUDENT_DATA, jsonStudentData)
            apply()
        }
    }

    fun getStudentData(): Student? {
        val jsonStudentData = sharedPreferences.getString(STUDENT_DATA, null)
        return Gson().fromJson(jsonStudentData, Student::class.java)
    }

    //Получение рейтинг плана с помощью RefreshToken
    fun getRatingPlanUsingRefreshToken(disciplineid: Int, callback: (StudentRatingPlan) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()
                val refreshedStudentTimeTable = userApi.getStudentRatingPlan("Bearer ${getAccessToken()}", disciplineid)
                saveStudentRatingPlan(refreshedStudentTimeTable)

                callback(refreshedStudentTimeTable)
            } catch (e: Exception) {
                e.printStackTrace()

                Log.e("error_global2", e.message.toString())
                Log.e("error_local2", e.localizedMessage)
            }
        }
    }

    //Обновление расписания с помощью RefreshToken
    fun refreshTimeTableDateUsingRefreshToken(date: String, callback: (List<StudentTimeTable>) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()
                val refreshedStudentRatingPlan = userApi.getStudentTimeTable("Bearer ${getAccessToken()}", date)
                saveStudentTimeTable(refreshedStudentRatingPlan)

                callback(refreshedStudentRatingPlan)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    fun getExpTime(): Long {
        return sharedPreferences.getLong(EXPIRATION_TIME, 0)
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

    //Создание клиента
    private fun createRetrofitApi(baseUrl: String): MrsuInterfaceApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MrsuInterfaceApi::class.java)
    }

    //Проверка на истечение времени использования токена
    private fun isAccessTokenExpired(): Boolean {
        val currentTime = System.currentTimeMillis()

        return currentTime >= getExpTime()
    }

    private fun checkTokenExpiration() {
        if (isAccessTokenExpired())
        {
            val BASE_URL_TOKEN = "https://p.mrsu.ru"
            val tokenApi = createRetrofitApi(BASE_URL_TOKEN)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userToken = tokenApi.getNewToken(refreshToken = getRefreshToken().toString())
                    saveToken(userToken)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
