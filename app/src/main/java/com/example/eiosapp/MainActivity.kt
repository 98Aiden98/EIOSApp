package com.example.eiosapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eiosapp.LayoutsScripts.bottom_nenu
import com.example.eiosapp.TokenPackage.MrsuInterfaceApi
import com.example.eiosapp.TokenPackage.SharedPrefManager
import com.example.eiosapp.TokenPackage.Token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private val BASE_URL_TOKEN = "https://p.mrsu.ru"
    private val BASE_URL_USER = "https://papi.mrsu.ru"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefManager = SharedPrefManager.getInstance(this)

        username = findViewById(R.id.login)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.LogButton)


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val tokenApi = createRetrofitClient(BASE_URL_TOKEN).create(MrsuInterfaceApi::class.java)
        val userApi = createRetrofitClient(BASE_URL_USER).create(MrsuInterfaceApi::class.java)

        loginButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userToken = tokenApi.getToken(
                        username = username.text.toString(),
                        password = password.text.toString()
                    )
                    handleTokenResponse(userToken, sharedPrefManager, userApi)
                } catch (e: Exception) {
                    handleTokenFailure(e)
                    Log.d("getUserToken_error", e.message.toString())
                }
            }
        }
    }

    private fun handleTokenFailure(throwable: Throwable) {
        Log.e("error_global", throwable.message.toString())
        Log.e("error_local", throwable.localizedMessage)
        runOnUiThread {
            showErrorToast("Ошибка при авторизации")
        }
    }

    private fun handleTokenResponse(userToken: Token, sharedPrefManager: SharedPrefManager, userApi: MrsuInterfaceApi) {
        if (userToken.accessToken != null) {
            sharedPrefManager.saveToken(userToken)
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val user = userApi.getUser("Bearer ${userToken.accessToken}")
                    sharedPrefManager.saveUserData(user)

                    val student = userApi.getStudent("Bearer ${userToken.accessToken}")
                    sharedPrefManager.saveStudentData(student)

                    val studentsemester = userApi.getStudentSemester("Bearer ${userToken.accessToken}")
                    sharedPrefManager.saveStudentSemester(studentsemester)

                    val studenttimetable = userApi.getStudentTimeTable("Bearer ${userToken.accessToken}", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                    sharedPrefManager.saveStudentTimeTable(studenttimetable)

                    runOnUiThread {
                        performActionsAfterAuthentication()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        showErrorToast("Ошибка при получении пользовательских данных в handleTokenResponse ")
                        Log.e("error_global", e.message.toString())
                        Log.e("error_local", e.localizedMessage)
                    }
                }
            }
        } else {
            runOnUiThread {
                showErrorToast("Ошибка при авторизации")
            }
        }
    }

    private fun performActionsAfterAuthentication() {
        val intent = Intent(this@MainActivity, bottom_nenu::class.java)
        startActivity(intent)
    }

    private fun createRetrofitClient(baseUrl: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
//    private fun GetUserData() {
//
//        val api = Retrofit.Builder()
//            .baseUrl(BaseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(MyApi::class.java)
//
//        api.getUserData().enqueue(object : Callback<User> {
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                if(response.isSuccessful){
//                    response.body()?.let{
//                            Log.i(TAG,"onResponse: ${response}")
//                        }
//                    }
//                }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Log.i(TAG,"onFailure: Ошибка")
//            }
//        })
//    }
}

