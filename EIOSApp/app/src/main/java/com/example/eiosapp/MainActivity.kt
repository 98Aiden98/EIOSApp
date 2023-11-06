package com.example.eiosapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun EnterToProfile(view: View)    {
        val intent = Intent(this, Profile::class.java)
        val login: String = findViewById<EditText?>(R.id.login).text.toString()
        val password: String = findViewById<EditText?>(R.id.password).text.toString()
            if (login == "РепинИА" && password == "123456") {
            startActivity(intent)
        }
        else if (login == "" && password == ""){
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, "Заполните все поля", duration)
            toast.show()
        }
        else {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, "Неверный логин или пароль", duration)
            toast.show()
        }
    }
}

