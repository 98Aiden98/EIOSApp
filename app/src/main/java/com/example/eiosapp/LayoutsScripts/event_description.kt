package com.example.eiosapp.LayoutsScripts

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eiosapp.R
import com.example.eiosapp.TokenPackage.SharedPrefManager
import com.example.sus.activity.logic.auth.retrofit.dto.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class event_description : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_description)
        val eventid = intent.getStringExtra("event")
        val title = intent.getStringExtra("title")
        val MainLayout: LinearLayout = findViewById(R.id.current_event_layout)
        val EventTitle: TextView = findViewById(R.id.currentEventTitle)
        EventTitle.setTextSize(15F)
        EventTitle.text = title
        val loadingIndicator = findViewById<ProgressBar>(R.id.loadingIndicatorforumMessage)
        loadingIndicator.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val currentEvent = suspendCoroutine { continuation ->
                    if (eventid != null) {
                        SharedPrefManager.refreshEventUsingRefreshToken(eventid) { result ->
                            continuation.resume(result)
                        }
                    }
                }
                showEvent(currentEvent, MainLayout)
                loadingIndicator.visibility = View.GONE
            }
            catch (e: Exception) {
                e.printStackTrace()
                Log.e("error_global2", e.message.toString())
                Log.e("error_local2", e.localizedMessage)
            }
        }
    }

    fun showEvent(event: Event,MainLayout: LinearLayout){
        val adminsSection = LinearLayout(this)
        adminsSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        adminsSection.orientation = LinearLayout.VERTICAL
        adminsSection.setPadding(10,10,10,10)
        for(i in 0..<event.admins.size) {
            val admins = event.admins[i].fullName
            val adminsTextView = TextView(this)
            adminsTextView.setTextColor(Color.WHITE)
            adminsTextView.text = admins
            adminsSection.addView(adminsTextView)
        }

        MainLayout.addView(adminsSection)

    }
}