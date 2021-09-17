package com.example.security


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val lsb : LinearLayout = findViewById(R.id.lsb)
        val extra : LinearLayout = findViewById(R.id.extra)
        val plus : LinearLayout = findViewById(R.id.plus)

        lsb.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_baseline_filter_center_focus_24)
        lsb.findViewById<TextView>(R.id.textView2).text = "LSB隐写"

        extra.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_baseline_forward_to_inbox_24)
        extra.findViewById<TextView>(R.id.textView2).text = "傅里叶"

        plus.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_baseline_filter_9_plus_24)
        plus.findViewById<TextView>(R.id.textView2).text = "傅里叶Plus"



        extra.setOnClickListener {
            startActivity(Intent(this,ExtraActivity::class.java))

        }


        lsb.setOnClickListener {
            startActivity(Intent(this,LSBActivity::class.java))

        }

        plus.setOnClickListener {
            startActivity(Intent(this,ExtraPlus::class.java))
        }




    }

   /* startActivity(Intent(this, WebActivity::class.java).also {
        it.putExtra("type","http://1.116.138.127:5000/photo/upload")
    })
    finish()*/


}