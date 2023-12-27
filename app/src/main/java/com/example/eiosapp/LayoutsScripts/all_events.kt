package com.example.eiosapp.LayoutsScripts

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eiosapp.R
import com.example.eiosapp.TokenPackage.SharedPrefManager
import com.example.sus.activity.logic.auth.retrofit.dto.EventInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class all_events : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allevents)
        SharedPrefManager.getInstance(this)

        val layout = findViewById(R.id.all_event_layout) as LinearLayout
        layout.orientation = LinearLayout.VERTICAL

        CoroutineScope(Dispatchers.Main).launch {
            val result = suspendCoroutine { continuation ->
                SharedPrefManager.refreshAllEventUsingRefreshToken() { result ->
                    continuation.resume(result)
                }
            }
            showEvents(result,layout)
        }

        /*val backButton: ImageButton = findViewById(R.id.arrow_back)
        backButton.setOnClickListener {
            onBackPressed()
        }*/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun showEvents(events: List<EventInfo>, layout: LinearLayout) {
        for (k in 0..<events.size) {
            val title =
                events[k].name // Название факультета
            val titleTextView = TextView(this)
            titleTextView.text = title
            titleTextView.gravity = Gravity.CENTER
            titleTextView.setTextColor(Color.WHITE)

            val eventTitle = "${events[k].startDate.substring(0,10)} - ${events[k].endDate.substring(0,10)} "
            val eventTextView = TextView(this)
            eventTextView.text = eventTitle
            eventTextView.setTextColor(Color.WHITE)
            eventTextView.gravity = Gravity.CENTER

            // Контейнер, содержащий заголовок факультета
            val titleLayout = LinearLayout(this)
            val titleLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            titleLayoutParams.setMargins(0, 50, 0, 0)
            titleLayout.layoutParams = titleLayoutParams
            titleLayout.setBackgroundResource(R.drawable.rating_plan_section_style)
            titleLayout.orientation = LinearLayout.VERTICAL
            titleLayout.setPadding(20, 10, 10, 10)
            titleLayout.gravity = Gravity.CENTER
            titleLayout.addView(titleTextView)
            titleLayout.addView(eventTextView)
            layout.addView(titleLayout)
            titleLayout.setOnClickListener{
                val eventid = events[k].id
                val intent = Intent(this@all_events, event_description::class.java)
                intent.putExtra("event", eventid.toString()) // Передача id дисциплины в файл Rating_plan
                intent.putExtra("title", title)
                startActivity(intent)
            }

        }
    }
}


