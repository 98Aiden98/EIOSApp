package com.example.eiosapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profile1.newInstance] factory method to
 * create an instance of this fragment.
 */
class profile1 : Fragment() {
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
        // Inflate the layout for this fragment
        SharedPrefManager.refreshDataUsingRefreshToken(SharedPrefManager.getRefreshToken().toString())
        val view: View = inflater.inflate(R.layout.fragment_profile1, container, false)
        val name: TextView = view.findViewById(R.id.Name)
        val studentIDTextView: TextView = view.findViewById(R.id.ID)
        val profilePictureImageView: ImageView = view.findViewById(R.id.ProfilePhoto)
        name.text = SharedPrefManager.getUserData()?.fio
        studentIDTextView.text = "ID: ${SharedPrefManager.getUserData()?.studentCod}"
        val profilePhotoUrl = SharedPrefManager.getUserData()?.photo?.urlMedium

        Glide.with(this)
            .load(profilePhotoUrl)
            .placeholder(R.drawable.noavatar)
            .transform(CenterCrop(), RoundedCorners(40))
            .into(profilePictureImageView)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profile1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profile1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}