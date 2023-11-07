package com.example.eiosapp.LayoutsScripts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.eiosapp.R
import com.example.eiosapp.TokenPackage.SharedPrefManager


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [marks.newInstance] factory method to
 * create an instance of this fragment.
 */
class marks : Fragment() {
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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_marks, container, false)
        val count: Int = SharedPrefManager.getStudentSemester()?.recordBooks?.get(0)?.discipline?.size!!
        val layout = view.findViewById(R.id.MarksLayout) as LinearLayout
        layout.orientation =
            LinearLayout.VERTICAL
            for (j in 0..<count) {
                val title: String = SharedPrefManager.getStudentSemester()?.recordBooks?.get(0)?.discipline?.get(j)?.title.toString()
                val btnTag = Button(context)
                btnTag.layoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                btnTag.setBackgroundResource(R.drawable.slidemenubutton)
                btnTag.text = title
                btnTag.id = j + 1 * 4
                layout.addView(btnTag)
            }

        /*var layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        val layout = view.findViewById(R.id.MarksLayout) as LinearLayout
        layout.orientation = LinearLayout.VERTICAL;
        layoutParams.setMargins(0, 24, 0, 24);
        val a = LinearLayout(context)
        for(i in 0..<count){
            val title: String = SharedPrefManager.getStudentSemester()?.recordBooks?.get(0)?.discipline?.get(i)?.title.toString()
            val b = Button(context)
            // Создаем параметры расположения и отступов для кнопок
            b.layoutParams = layoutParams
            b.setTextColor(Color.WHITE)
            b.setBackgroundResource(R.drawable.slidemenubutton)
            b.text = title
            b.id = i
            a.addView(b)
        }
        layout.addView(a)*/
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment marks.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            marks().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}