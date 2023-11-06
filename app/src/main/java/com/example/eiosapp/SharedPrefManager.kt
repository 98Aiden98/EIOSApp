package com.example.eiosapp
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SharedPrefManager {
    private const val PREF_NAME = "MyPrefs"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val STUDENT_DATA = "student_data"
    private const val USER_DATA = "user_data"
    private const val STUDENT_SEMESTER = "student_semester"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var instance: SharedPrefManager

    fun getInstance(context: MainActivity): SharedPrefManager {
        if (!this::instance.isInitialized) {
            instance = SharedPrefManager
            init(context)
        }
        return instance
    }

    private fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val refreshToken = REFRESH_TOKEN
        if (refreshToken != null) {
            refreshDataUsingRefreshToken(refreshToken)
        }
    }

    fun refreshDataUsingRefreshToken(refreshToken: String) {
        val BASE_URL_TOKEN = "https://p.mrsu.ru"
        val BASE_URL_USER = "https://papi.mrsu.ru"

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val tokenApi = createRetrofitClient(BASE_URL_TOKEN).create(MrsuInterfaceApi::class.java)
        val userApi = createRetrofitClient(BASE_URL_USER).create(MrsuInterfaceApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userToken = tokenApi.getNewToken(refreshToken = refreshToken)
                saveTokens(userToken.accessToken, userToken.refreshToken)

                val refreshedData = userApi.getUser("Bearer ${userToken.accessToken}")
                saveUserData(refreshedData)

            } catch (e: Exception) {

            }
        }

    }
    fun createRetrofitClient(baseUrl: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
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

    fun getUserData(): User? {
        val jsonUserData = sharedPreferences.getString(USER_DATA, null)
        return Gson().fromJson(jsonUserData, User::class.java)
    }

    fun getStudentSemester(): StudentSemester? {
        val jsonStudentSemester = sharedPreferences.getString(STUDENT_SEMESTER, null)
        return Gson().fromJson(jsonStudentSemester, StudentSemester::class.java)
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

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    fun clearTokens() {
        sharedPreferences.edit().apply {
            remove(ACCESS_TOKEN)
            remove(REFRESH_TOKEN)
            remove(USER_DATA)
            remove(STUDENT_DATA)
            apply()
        }
    }
}
