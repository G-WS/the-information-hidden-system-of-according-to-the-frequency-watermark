package com.example.security

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.ByteArrayOutputStream

class TextActivitiy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_activitiy)
        val edit:EditText = findViewById(R.id.editText)
        val back:Button = findViewById(R.id.back)
        val get:Button = findViewById(R.id.get)


        back.setOnClickListener {
            Been.selectBitmap = null
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


        get.setOnClickListener {
            if (edit.text.toString() == "") Toast.makeText(this, "请输入需要加密内容", Toast.LENGTH_SHORT).show()
            else{
                Been.word = edit.text.toString()
            }
        }




    }
}