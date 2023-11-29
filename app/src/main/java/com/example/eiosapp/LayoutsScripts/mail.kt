package com.example.eiosapp.LayoutsScripts

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.eiosapp.R
import com.example.eiosapp.TokenPackage.SharedPrefManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [mail.newInstance] factory method to
 * create an instance of this fragment.
 */
class mail : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_mail, container, false)
        val layout = view.findViewById(R.id.ChatLayout) as LinearLayout
        layout.orientation = LinearLayout.VERTICAL

        // Цикл по количеству факультетов
        // (У кого-то кроме своего факультета могут быть доп. пары с другого факультета. Например, УМУ)
        for(k in 0..<SharedPrefManager.getStudentSemester()?.recordBooks?.size!! ) {
            val title = SharedPrefManager.getStudentSemester()?.recordBooks?.get(k)?.faculty // Название факультета
            val titleTextView = TextView(context)
            titleTextView.text = title
            titleTextView.setTextColor(Color.WHITE)

            // Контейнер, содержащий заголовок факультета
            val titleLayout = LinearLayout(context)
            titleLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            titleLayout.setBackgroundResource(R.drawable.rating_plan_section_style)
            titleLayout.orientation = LinearLayout.HORIZONTAL
            titleLayout.setPadding(20,10,10,10)
            titleLayout.gravity = Gravity.CENTER
            titleLayout.addView(titleTextView)
            layout.addView(titleLayout)

            val count: Int =
                SharedPrefManager.getStudentSemester()?.recordBooks?.get(k)?.discipline?.size!! // Количество дисциплин
            for (j in 0..<count) {
                // Название дисциплины
                val title: String = SharedPrefManager.getStudentSemester()?.recordBooks?.get(k)?.discipline?.get(j)?.title.toString()

                val btnTag = Button(context)
                btnTag.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                btnTag.setBackgroundResource(R.drawable.slidemenubutton)
                btnTag.text = title
                btnTag.id = j + 1 * 4
                layout.addView(btnTag)

                // При нажатии на кнопку открывать рейтинг-план по выбранной дисциплине
                btnTag.setOnClickListener {
                    val disciplineid: String = SharedPrefManager.
                    getStudentSemester()?.recordBooks?.get(k)?.discipline?.get(j)?.id.toString()

                    val intent = Intent(context, forum_message::class.java)
                    intent.putExtra("disciplineidForumMessage", disciplineid) // Передача id дисциплины в файл Rating_plan
                    intent.putExtra("titleForumMessage", title) // Передача названия дисциплины в файл Rating_plan
                    startActivity(intent)
                }


            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment mail.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            mail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}