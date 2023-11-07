package com.example.eiosapp.LayoutsScripts

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.eiosapp.R
import com.example.eiosapp.TimeTablePackage.StudentTimeTable
import com.example.eiosapp.TokenPackage.SharedPrefManager
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var dateTV: TextView
private lateinit var calendarView: CalendarView
private lateinit var tableLayout: LinearLayout
/**
 * A simple [Fragment] subclass.
 * Use the [Schedule.newInstance] factory method to
 * create an instance of this fragment.
 */
class Schedule : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var materialCalendarView: MaterialCalendarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = null
        view = inflater.inflate(R.layout.fragment_schedule, container, false)
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        dateTV = view.findViewById(R.id.idTVDate)
        tableLayout = view.findViewById(R.id.timetable_tableLayout)
        tableLayout.orientation = LinearLayout.VERTICAL
        calendarView = view.findViewById(R.id.calendarView)
        context?.let { SharedPrefManager.getInstance(it).refreshDataUsingRefreshToken() }
        val studentTimeTable = SharedPrefManager.getStudentTimeTable()
        if (studentTimeTable != null) {
            createTimeTable(studentTimeTable)
        }
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            tableLayout.removeAllViews()
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
            val formattedDate = dateFormat.format(calendar.time)
            dateTV.text = formattedDate
            CoroutineScope(Dispatchers.Main).launch {

                val studentTimeTable: List<StudentTimeTable> = suspendCoroutine { continuation ->
                    SharedPrefManager.refreshTimeTableDateUsingRefreshToken(
                        formattedDate
                    ) { studentTimeTable ->
                        continuation.resume(studentTimeTable)
                    }
                }

                createTimeTable(studentTimeTable)
            }


            /*tableLayout.removeAllViews()
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
            val formattedDate = dateFormat.format(calendar.time)
            dateTV.text = formattedDate

            val loadingIndicator = view.findViewById<ProgressBar>(R.id.loadingIndicator)
            loadingIndicator.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {

                val studentTimeTable: List<StudentTimeTable> = suspendCoroutine { continuation ->
                    SharedPrefManager.refreshTimeTableDateUsingRefreshToken(
                        formattedDate
                    ) { studentTimeTable ->
                        continuation.resume(studentTimeTable)
                    }
                }

                createTimeTable(studentTimeTable)
                loadingIndicator.visibility = View.GONE
            }*/
        }
        return view
    }

    private fun createTimeTable(studentTimeTable: List<StudentTimeTable>) {
        val lessonsCount = intArrayOf(0, 1, 2, 3, 4, 5, 6)
        val schedule = arrayOf(
            "8:30 - 10:00",
            "10:10 - 11:40",
            "12:00 - 13:30",
            "13:45 - 15:15",
            "15:25 - 16:55",
            "17:05 - 18:35",
            "18:40 - 20:10"
        )
        studentTimeTable?.let { timeTableList ->
            var prevGroupName = ""

            for (studentTimeTable in timeTableList) {
                val groupName = studentTimeTable.group
                val timeTable = studentTimeTable.timeTable
                if (groupName != prevGroupName) {
                    var lessonsCounter = 0
                    for(i in lessonsCount.indices) {
                        val a = LinearLayout(context)
                        a.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        a.setBackgroundResource(R.drawable.slidemenubutton)
                        a.orientation = LinearLayout.VERTICAL
                        val c = LinearLayout(context)
                        c.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        c.orientation = LinearLayout.VERTICAL
                        c.setPadding(20, 0,0,0)
                        val lesNum = TextView(context)
                        lesNum.text = "${(i+1)}. ${schedule[i]}"
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        lesNum.setTextColor(Color.WHITE)
                        c.addView(lesNum)
                        c.id = i+1*4
                        a.id = i + 1 * 4
                        val b = LinearLayout(context)
                        b.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        b.id = i+1*4
                        b.orientation = LinearLayout.HORIZONTAL
                        val d = LinearLayout(context)
                        d.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        d.orientation = LinearLayout.HORIZONTAL
                        d.setPadding(20, 20, 20, 20)
                        val e = LinearLayout(context)
                        e.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        e.orientation = LinearLayout.VERTICAL
                        e.gravity = Gravity.CENTER_VERTICAL
                        if(lessonsCounter < timeTable.lessons.size) {
                            if ((i + 1).toByte() == timeTable.lessons[lessonsCounter].number) {
                                val photoimageview = ImageView(context)
                                d.addView(photoimageview)
                                val TeacherPhoto =
                                    timeTable.lessons[lessonsCounter].disciplines[0].teacher.photo.urlSmall
                                val LessonTitle = TextView(context)
                                LessonTitle.setTextColor(Color.WHITE)
                                LessonTitle.text = timeTable.lessons[lessonsCounter].disciplines[0].title
                                val TeacherName = TextView(context)
                                TeacherName.setTextColor(Color.WHITE)
                                TeacherName.text = timeTable.lessons[lessonsCounter].disciplines[0].teacher.name
                                val Classroom = TextView(context)
                                Classroom.text = "Аудитория ${timeTable.lessons[lessonsCounter].disciplines[0].auditorium.number}. корп. ${timeTable.lessons[lessonsCounter].disciplines[0].auditorium.campustitle}"
                                Classroom.setTextColor(Color.WHITE)
                                Glide.with(this)
                                .load(TeacherPhoto)
                                .transform(CenterCrop(), RoundedCorners(80))
                                .apply(RequestOptions().override(160, 160))
                                .into(photoimageview)

                                e.addView(LessonTitle)
                                e.addView(TeacherName)
                                e.addView(Classroom)
                                lessonsCounter += 1
                            }
                        }
                        b.addView(d)
                        b.addView(e)
                        a.addView(c)
                        a.addView(b)
                        tableLayout.addView(a)
                    }
                }
            }

        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment schedule.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Schedule().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

    /*@SuppressLint("SetTextI18n")
    private fun createTimeTable(studentTimeTable: List<StudentTimeTable>?) {
        tableLayout.removeAllViews()
        val paddingInPixels = 20
        val paddingInDp = context?.let { paddingInPixels.convertPixelsToDp(it) }
        val fontSize = 14f

        fun addEmptyRow(lessonNumber: Int) {
            val emptyRow = TableRow(context)
            emptyRow.setBackgroundResource(R.drawable.table_border)

            val emptyCell = TextView(context)
            emptyCell.text = lessonNumber.toString()
            context?.let {emptyCell.setTextColor( ContextCompat.getColor(it, R.color.white) )}
            if (paddingInDp != null) {
                emptyCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
            }
            emptyCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
            emptyCell.setBackgroundResource(R.drawable.timetable_table_id_cell_border)

            val emptyCellLayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            emptyCell.setLayoutParams(emptyCellLayoutParams)

            emptyRow.addView(emptyCell)

            val emptyTitleCell = TextView(context)
            emptyTitleCell.text = ""
            emptyTitleCell.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            if (paddingInDp != null) {
                emptyTitleCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
            }
            emptyTitleCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

            emptyRow.addView(emptyTitleCell)

            tableLayout.addView(emptyRow)
        }

        studentTimeTable?.let { timeTableList ->
            var prevGroupName = ""
            var maxLessonNumber = -1

            for (studentTimeTable in timeTableList) {
                val groupName = studentTimeTable.group
                val timeTable = studentTimeTable.timeTable


                if (groupName != prevGroupName) {
                    val separatorRow = TableRow(context)
                    val separatorCell = TextView(context)
                    separatorCell.text = ""
                    if (paddingInDp != null) {
                        separatorCell.setPadding(0, paddingInDp * 2, 0, paddingInDp * 2)
                    }
                    separatorRow.addView(separatorCell)
                    tableLayout.addView(separatorRow)
                    prevGroupName = groupName
                }

                val groupRow = TableRow(context)
                groupRow.setBackgroundColor(Color.WHITE)
                val groupLayoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                groupLayoutParams.setMargins(7, 7, 7, 7)
                groupRow.layoutParams = groupLayoutParams

                val groupCell = TextView(context)
                groupCell.text = " (${groupName}) \n"
                context?.let {groupCell.setTextColor( ContextCompat.getColor(it, R.color.white) )}
                if (paddingInDp != null) {
                    groupCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
                }
                groupCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

                val groupCellLayoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                groupCell.setLayoutParams(groupCellLayoutParams)

                groupRow.addView(groupCell)

                val facultyCell = TextView(context)
                facultyCell.text = studentTimeTable.facultyName
                context?.let { facultyCell.setTextColor(ContextCompat.getColor(it, R.color.white) )}
                if (paddingInDp != null) {
                    facultyCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
                }
                facultyCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                context?.let { facultyCell.setMaxWidth(it) }

                TextViewCompat.setAutoSizeTextTypeWithDefaults(
                    facultyCell,
                    TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
                )

                groupRow.setBackgroundResource(R.color.white)

                groupRow.addView(facultyCell)

                tableLayout.addView(groupRow)

                if (timeTable.lessons.isEmpty()) {
                    continue
                }

                if (timeTable.lessons.isNotEmpty()) {
                    val firstLessonNumber = timeTable.lessons[0].number.toInt()
                    if (firstLessonNumber > 1) {
                        for (i in 1 until firstLessonNumber) {
                            addEmptyRow(i)
                        }
                    }
                }

                for (lesson in timeTable.lessons) {
                    if (lesson.disciplines.isEmpty()) {
                        addEmptyRow(lesson.number.toInt())
                        continue
                    }

                    if (maxLessonNumber != -1 && lesson.number - maxLessonNumber > 1) {
                        for (i in maxLessonNumber + 1 until lesson.number) {
                            addEmptyRow(i)
                        }
                    }

                    for (discipline in lesson.disciplines) {
                        val lessonRow = TableRow(context)
                        lessonRow.setBackgroundResource(R.drawable.table_border)

                        val idCell = TextView(context)
                        idCell.text = lesson.number.toString() + "\n"
                        idCell.setBackgroundResource(R.drawable.timetable_table_id_cell_border)
                        context?.let { idCell.setTextColor(ContextCompat.getColor(it, R.color.white) )}
                        if (paddingInDp != null) {
                            idCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
                        }
                        idCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

                        lessonRow.addView(idCell)

                        val titleCell = TextView(context)
                        titleCell.text =
                            "${discipline.title}\n[к.${discipline.auditorium.campusid.toString()[0]} ${discipline.auditorium.number}] (${discipline.teacher.name})"

                        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                            titleCell,
                            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
                        )

                        context?.let { titleCell.setMaxWidth(it) }
                        if (paddingInDp != null) {
                            titleCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
                        }
                        titleCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

                        lessonRow.addView(titleCell)
                        tableLayout.addView(lessonRow)
                    }
                    if (lesson.number > maxLessonNumber) {
                        maxLessonNumber = lesson.number.toInt()
                    }

                }
                val MAX_LESSON_NUMBER = studentTimeTable.timeTable.lessons.maxOfOrNull { it.number } ?: 0

                if (maxLessonNumber < MAX_LESSON_NUMBER) {
                    for (i in maxLessonNumber + 1..MAX_LESSON_NUMBER) {
                        addEmptyRow(i)
                    }

                }
            }
        }
    }
}


private fun Int.convertPixelsToDp(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

private fun TextView.setMaxWidth(context: Context) {
    val maxWidth = (context.resources.displayMetrics.widthPixels * 0.73).toInt()
    this.maxWidth = maxWidth
}*/