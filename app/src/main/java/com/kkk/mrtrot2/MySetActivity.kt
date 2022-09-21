package com.kkk.mrtrot2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.reflect.Array.getInt
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat


class MySetActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_set)

        val pref = getSharedPreferences("pref",0)
        val nickName = pref.getString("Nickname","")
        val edit = pref.edit()
        val nowId = findViewById<TextView>(R.id.currentNickname)

        nowId.text=nickName

        val changeBtn = findViewById<Button>(R.id.change_button)
        changeBtn.setOnClickListener {
            val changeName = findViewById<EditText>(R.id.changeNickname)
            val nickname = changeName.text.toString()

            if(nickname ==""){
                Toast.makeText(this, "닉네임을 정해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                edit.putString("Nickname", nickname)
                edit.apply()
                nowId.text = nickname
                changeName!!.text.clear()
            }
        }

        val questionBtn = findViewById<TextView>(R.id.questionText)
        questionBtn.setOnClickListener {

            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            selectorIntent.data = Uri.parse("mailto:")

            selectorIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("sookimy97@gmail.com"))
            selectorIntent.putExtra(Intent.EXTRA_SUBJECT, "미스터트롯2 응원하기 문의 메일")
            selectorIntent.putExtra(Intent.EXTRA_TEXT,  "문의하실 사항을 적어주세요")

            startActivity(Intent.createChooser(selectorIntent, "이메일 보내기"))
        }

        val useSite = findViewById<TextView>(R.id.useRule)
        useSite.setOnClickListener {
            val uri = Uri.parse("https://sites.google.com/view/mrtrot2use/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val infoSite = findViewById<TextView>(R.id.infoRule)
        infoSite.setOnClickListener {
            val uri = Uri.parse("https://sites.google.com/view/zensprivacy/%ED%99%88")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}