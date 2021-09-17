package com.example.security

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.*


class ExtraActivity : AppCompatActivity() {
    val PERMISSIONS = arrayOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra)
        val send: Button = findViewById(R.id.send)
        val extract: Button = findViewById(R.id.extract)
        val result: Button = findViewById(R.id.result)
        val mark: Button = findViewById(R.id.mark)

        doCheckPermission()
        send.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java).also {
                it.putExtra("type", "http://1.116.138.127:5002/send")
            })
        }

        extract.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java).also {
                it.putExtra("type", "http://1.116.138.127:5002/extract")
            })
        }

        result.setOnClickListener {
        /*    startActivity(Intent(this, WebActivity::class.java).also {
                it.putExtra("type", "http://1.116.138.127:5002/photo/result.jpg")
            })*/
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = getPic("http://1.116.138.127:5002/photo/result.jpg")
                if (bitmap != null) {
                    saveBitmap(bitmap, "Extra.jpg")
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(applicationContext, "保存成功", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        mark.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = getPic("http://1.116.138.127:5002/photo/watermark.jpg")
                if (bitmap != null) {
                    saveBitmap(bitmap, "WaterMark.jpg")
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
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
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