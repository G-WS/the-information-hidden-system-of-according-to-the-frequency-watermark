package com.example.security

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.*

class ExtraPlus : AppCompatActivity() {
    val PERMISSIONS = arrayOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_plus)
        doCheckPermission()

        val send: Button = findViewById(R.id.plus_en)
        val extract: Button = findViewById(R.id.plus_geten)
        val result: Button = findViewById(R.id.plus_de)
        val mark: Button = findViewById(R.id.plus_getde)

        send.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java).also {
                it.putExtra("type", "http://1.116.138.127:5003/send")
            })
        }

        extract.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = getPic("http://1.116.138.127:5003/photo/watermarked_image.png")
                if (bitmap != null) {
                    saveBitmap(bitmap, "ExtraPlusEn.png")
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(applicationContext, "保存成功", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        result.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java).also {
                it.putExtra("type", "http://1.116.138.127:5003/extract")
            })

        }

        mark.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = getPic("http://1.116.138.127:5003/photo/extracted_watermark.png")
                if (bitmap != null) {
                    saveBitmap(bitmap, "ExtraPlusDe.png")
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(applicationContext, "保存成功", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



    }
    private fun getPic(url: String): Bitmap? {
        try {
            val client = OkHttpClient()
            val request: Request = Request.Builder().url(url).build()
            val body = client.newCall(request).execute().body()
            val `in`: InputStream = body!!.byteStream()
            return BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    private fun saveBitmap(bitmap: Bitmap, bitName: String) {
        val file = File("/sdcard/DCIM/Camera/$bitName")
        if (file.exists()) {
            file.delete()
        }
        val out: FileOutputStream
        try {
            out = FileOutputStream(file)
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush()
                out.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    //检查所需的全部权限
    fun doCheckPermission(): Boolean {
        val mPermissionsChecker = PermissionsChecker(this)
        if (mPermissionsChecker.lacksPermissions(*PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 0x12)
            return false
        }
        return true
    }
}