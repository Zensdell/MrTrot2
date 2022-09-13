package com.kkk.mrtrot2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast



class FragmentMySet : Fragment() {

    private val TAG = "FragmentMySet"



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view =  inflater.inflate(R.layout.fragment_my_set, container, false) as ViewGroup
        val pref = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val nickName = pref.getString("Nickname","")
        val edit = pref.edit()
        val nowId = view.findViewById<TextView>(R.id.currentNickname)

        if (nowId != null) {
            nowId.text=nickName
        }

        val changeBtn = view.findViewById<Button>(R.id.change_button)
        changeBtn?.setOnClickListener {
            val changeName = view.findViewById<EditText>(R.id.changeNickname)
            val nickname = changeName?.text.toString()

            if(nickname ==""){
                Toast.makeText(context, "닉네임을 정해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                edit.putString("Nickname", nickname)
                edit.apply()
                if (nowId != null) {
                    nowId.text = nickname
                }
                changeName!!.text.clear()
            }
        }

        val questionBtn = view.findViewById<TextView>(R.id.questionText1)
        questionBtn?.setOnClickListener {

            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            selectorIntent.data = Uri.parse("mailto:")

            selectorIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("sookimy97@gmail.com"))
            selectorIntent.putExtra(Intent.EXTRA_SUBJECT, "국민가수 응원하기 문의 메일")
            selectorIntent.putExtra(Intent.EXTRA_TEXT,  "문의하실 사항을 적어주세요")

            startActivity(Intent.createChooser(selectorIntent, "이메일 보내기"))
        }

        val useSite = view.findViewById<TextView>(R.id.useRule1)
        useSite?.setOnClickListener {
            Log.d(TAG, "어??여기서 찍어봐야될거아냐 그래야 알수있는거아냐!!!! ")
            val uri = Uri.parse("https://sites.google.com/view/useful3/%ED%99%88")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val infoSite =view.findViewById<TextView>(R.id.infoRule1)
        infoSite?.setOnClickListener {
            Log.d(TAG, "어??여기서 찍어봐야될거아냐 그래야 알수있는거아냐2222222 ")
            val uri = Uri.parse("https://sites.google.com/view/zensprivacy/%ED%99%88")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        return view
    }


}