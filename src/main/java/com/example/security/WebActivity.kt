package com.example.security


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


const val SELECT_IMAGE_REQUEST_CODE = 1000
private var mValueCallback: ValueCallback<Array<Uri>>? = null

class WebActivity : AppCompatActivity() {
    lateinit var dialog: AlertDialog
    lateinit var webView:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val intent = intent.getStringExtra("type") ?: "https://www.baidu.com/"
        init()

        loading()
        webView = findViewById<WebView>(R.id.webView)



        webView.settings.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0";
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(true)
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        webView.loadUrl(intent)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                dismissloading()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loading()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                url?.let {
                    try {
                        if (url.startsWith("bilibili://")) {
                            Toast.makeText(applicationContext, "catch ! ! ", Toast.LENGTH_SHORT)
                                .show()
                            Log.d(
                                "hhhhhhhhhhhhhhhhhhhh",
                                "shouldOverrideUrlLoading:${Uri.parse(url)} "
                            )
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                            webView.loadUrl(url)
                            return true
                        }
                    } catch (e: Exception) {
                        return false
                    }
                    webView.loadUrl(url)
                    return true
                }
                return true
            }


        }


    }

    fun init() {
        dialog = AlertDialog.Builder(this).also {
            it.setMessage("?????????????????????...")
        }.create()
    }

    fun loading() {

        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismissloading() {
        dialog.dismiss()
    }


    inner class MyWebChromeClient : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView,
            valueCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            resetWebSelectImageCallBack()
            mValueCallback = valueCallback
            selectImage()
            return true
        }
    }


    fun selectImage() {
        val intent = Intent()
        /* ??????Pictures??????Type?????????image */
        /* ??????Pictures??????Type?????????image */intent.type = "image/*"
        /* ??????Intent.ACTION_GET_CONTENT??????Action */
        /* ??????Intent.ACTION_GET_CONTENT??????Action */intent.action = Intent.ACTION_GET_CONTENT
        /* ?????????????????????????????? */
        /* ?????????????????????????????? */startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (RESULT_OK != resultCode) {
                resetWebSelectImageCallBack()
                Toast.makeText(this, "??????", Toast.LENGTH_SHORT).show()
                return
            }
            if (null != mValueCallback) {
                val uri = data?.data
                Toast.makeText(this, "??????", Toast.LENGTH_SHORT).show()
                mValueCallback!!.onReceiveValue(arrayOf(uri!!))
                mValueCallback = null
                return
            }
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()

    }

    private fun resetWebSelectImageCallBack() {
        if (null != mValueCallback) {
            mValueCallback!!.onReceiveValue(null)
            mValueCallback = null
        }
    }
    var exitTime = 0L
    override fun onBackPressed() {
        //???????????????????????????????????????????????????????????????

        if (webView.canGoBack()) {
            webView.goBack()
        } else {

            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(applicationContext, "??????????????????????????????", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                finish()
            }
        }
    }
}
