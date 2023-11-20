package com.example.eiosapp

import UserManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.eiosapp.LayoutsScripts.bottom_nenu
import com.example.eiosapp.TokenPackage.MrsuInterfaceApi
import com.example.eiosapp.TokenPackage.SharedPrefManager
import com.example.eiosapp.TokenPackage.Token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
    lateinit var userManager: UserManager
    private var pass = ""
    private var name = ""
    //Получение API
    val tokenApi = createRetrofitClient(BASE_URL_TOKEN).create(MrsuInterfaceApi::class.java)
    val userApi = createRetrofitClient(BASE_URL_USER).create(MrsuInterfaceApi::class.java)


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPrefManager = SharedPrefManager.getInstance(this)

        username = findViewById(R.id.login)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.LogButton)

        userManager = UserManager(this)
        observeData()
        val d = password.getText().toString()
        val p = username.getText().toString()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        if(username.text.toString() != "" && password.text.toString() != "")
        {

        }
        val h = username.text
        val g = password.text
        //При нажатии на кнопку 'Войти'
        loginButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userToken = tokenApi.getToken(
                        username = username.text.toString(),
                        password = password.text.toString()
                    )
                    handleTokenResponse(userToken, sharedPrefManager, userApi)
                    GlobalScope.launch {
                        userManager.storeUser(password.text.toString(), username.text.toString())
                    }
                } catch (e: Exception) {
                    handleTokenFailure(e)
                    Log.d("getUserToken_error", e.message.toString())
                }
            }
        }
    }

    private fun observeData() {
        // Updates age
        // every time user age changes it will be observed by userAgeFlow
        // here it refers to the value returned from the userAgeFlow function
        // of UserManager class
        this.userManager.userAgeFlow.asLiveData().observe(this) {
            pass = it.toString()
            password.setText(it.toString())
        }

        // Updates name
        // every time user name changes it will be observed by userNameFlow
        // here it refers to the value returned from the usernameFlow function
        // of UserManager class
        this.userManager.userNameFlow.asLiveData().observe(this) {
            name = it.toString()
            username.setText(it.toString())
        }
    }


    //Обработчик ошибки при входе
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

                    //Получение информации о пользователе
                    val user = userApi.getUser("Bearer ${userToken.accessToken}")
                    sharedPrefManager.saveUserData(user)

                    //Получение информации о студенте
                    val student = userApi.getStudent("Bearer ${userToken.accessToken}")
                    sharedPrefManager.saveStudentData(student)

                    //Получение информации о дисциплинах студента
                    val studentsemester = userApi.getStudentSemester("Bearer ${userToken.accessToken}")
                    sharedPrefManager.saveStudentSemester(studentsemester)

                    //Получение информации о расписании студента
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

    //Переход между окнами
    private fun performActionsAfterAuthentication() {
        val intent = Intent(this@MainActivity, bottom_nenu::class.java)
        startActivity(intent)
    }

    //Создание клиента
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
}

