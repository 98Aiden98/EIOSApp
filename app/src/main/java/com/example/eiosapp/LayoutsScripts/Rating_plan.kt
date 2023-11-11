package com.example.eiosapp.LayoutsScripts

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eiosapp.R
import com.example.eiosapp.StudentRatingPlan.StudentRatingPlan
import com.example.eiosapp.TokenPackage.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class rating_plan: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_plan)
        val disciplineid = intent.getStringExtra("disciplineid")
        val title = intent.getStringExtra("title")
        val sr: TextView = findViewById(R.id.rating_plan_discipline_name)
        sr.text = title
        CoroutineScope(Dispatchers.Main).launch {
            val studentRatingPlan: StudentRatingPlan = suspendCoroutine { continuation ->
                if (disciplineid != null) {
                    SharedPrefManager.getRatingPlanUsingRefreshToken(
                        disciplineid.toInt()
                    ) { studentTimeTable ->
                        continuation.resume(studentTimeTable)
                    }
                }
            }
            createRatingPlanTable(studentRatingPlan)
        }

    }

    private fun createRatingPlanTable(studentRatingPlan: StudentRatingPlan)
    {
        val MainLayout: LinearLayout = findViewById(R.id.rating_plan_layout)
        val MainTotalLayout: LinearLayout = findViewById(R.id.rating_plan_total)

        val totalInfoSection = LinearLayout(this)
        totalInfoSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,
            25F
        )
        totalInfoSection.orientation = LinearLayout.VERTICAL

        val totalBallSection = LinearLayout(this)
        totalBallSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 75F)
        totalBallSection.setBackgroundResource(R.drawable.rating_plan_section_style)
        totalBallSection.orientation = LinearLayout.HORIZONTAL
        totalBallSection.gravity = Gravity.CENTER

        var ballCounter = 0F
        var maxBallCounter = 0F
        for(i in 0..<studentRatingPlan.sections.size)
        {
            val MainSection = LinearLayout(this)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,0,0,50)
            MainSection.layoutParams = params
            MainSection.setBackgroundResource(R.drawable.slidemenubutton)
            MainSection.orientation = LinearLayout.HORIZONTAL

            val ControlDotSection = LinearLayout(this)
            ControlDotSection.setBackgroundResource(R.drawable.slidemenubutton)
            ControlDotSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,
                20F
            )
            ControlDotSection.orientation = LinearLayout.VERTICAL
            val rawtitle = studentRatingPlan.sections[i].title
            val title = rawtitle.replace("\n", "")
            val titleTextView = TextView(this)
            titleTextView.setTextColor(Color.WHITE)
            titleTextView.text = title

            val cdsTitle = LinearLayout(this)
            cdsTitle.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            cdsTitle.setBackgroundResource(R.drawable.rating_plan_section_style)
            cdsTitle.orientation = LinearLayout.HORIZONTAL
            cdsTitle.setPadding(20,10,10,10)
            cdsTitle.addView(titleTextView)
            ControlDotSection.addView(cdsTitle)
            for(j in 0..<studentRatingPlan.sections[i].controlDots.size) {
                val cdsInfo = LinearLayout(this)
                val cdsparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                cdsparams.setMargins(0,0,0,5)
                cdsInfo.layoutParams = cdsparams
                cdsInfo.setPadding(10,10,10,5)
                cdsInfo.orientation = LinearLayout.HORIZONTAL
                val infoSection = LinearLayout(this)
                infoSection.gravity = Gravity.CENTER_VERTICAL
                infoSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 20F)
                infoSection.orientation = LinearLayout.VERTICAL
                var formattedDate = "-"
                if(studentRatingPlan.sections[i].controlDots[j].date != null) {
                    val deadline = studentRatingPlan.sections[i].controlDots[j].date
                    formattedDate = deadline.substring(0,10)
                }
                var ball = "-"
                if (studentRatingPlan.sections[i].controlDots[j].mark != null) {
                    ball = studentRatingPlan.sections[i].controlDots[j].mark.ball.toString()
                    ballCounter += ball.toFloat()
                }
                val maxball = studentRatingPlan.sections[i].controlDots[j].maxBall
                maxBallCounter += maxball.toFloat()
                val deadlineTextView = TextView(this)
                val ballTextView = TextView(this)
                deadlineTextView.setTextColor(Color.WHITE)
                ballTextView.setTextColor(Color.WHITE)
                ballTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                ballTextView.gravity = Gravity.CENTER

                var worktitle = ""
                if(studentRatingPlan.sections[i].controlDots[j].title != null) {
                    val rawworktitle = studentRatingPlan.sections[i].controlDots[j].title
                    worktitle = rawworktitle.replace("\n", "")
                }
                val worktitleTextView = TextView(this)
                worktitleTextView.text = worktitle
                worktitleTextView.setTextColor(Color.WHITE)

                deadlineTextView.text = "Срок сдачи: ${formattedDate}"
                ballTextView.text = "${ball}/${maxball} "
                infoSection.addView(worktitleTextView)
                infoSection.addView(deadlineTextView)
                val BallSection = LinearLayout(this)
                BallSection.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 80F)
                BallSection.setBackgroundResource(R.drawable.rating_plan_ball_style)
                BallSection.orientation = LinearLayout.VERTICAL
                BallSection.gravity = Gravity.CENTER

                BallSection.addView(ballTextView)
                cdsInfo.addView(infoSection)
                cdsInfo.addView(BallSection)
                ControlDotSection.addView(cdsInfo)
            }

            MainSection.addView(ControlDotSection)

            MainLayout.addView(MainSection)
        }
        var markZeroSession = "-"
        if(studentRatingPlan.markZeroSession != null){
            markZeroSession = studentRatingPlan.markZeroSession.ball.toString()
        }
        val totalTextView = TextView(this)
        totalTextView.setTextColor(Color.WHITE)
        totalTextView.text = "Оценка за нулевую сессию: ${markZeroSession}/5.0"
        totalInfoSection.addView(totalTextView)

        val totalBallTextView = TextView(this)
        totalBallTextView.setTextColor(Color.WHITE)
        totalBallTextView.text = "${ballCounter}/${maxBallCounter}"

        totalBallSection.addView(totalBallTextView)

        MainTotalLayout.addView(totalInfoSection)
        MainTotalLayout.addView(totalBallSection)
    }
}