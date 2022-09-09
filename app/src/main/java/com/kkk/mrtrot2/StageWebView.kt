package com.kkk.mrtrot2

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout

class StageWebView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_web_view)

        var stageUrl = intent.getStringExtra("stageUrl")

        val stageWebView = findViewById<WebView>(R.id.stage_webview)
        stageWebView.webViewClient = WebViewClient()

        if (stageUrl != null) {
            stageWebView.getSettings().setDomStorageEnabled(true)
            stageWebView.getSettings().setJavaScriptEnabled(true)
            stageWebView.loadUrl(stageUrl)
        }

        stageWebView.webChromeClient = MyWebClient()


    }


    override fun onPause() {
        val stageWebView = findViewById<WebView>(R.id.stage_webview)
        super.onPause()
        if (stageWebView != null) {
            stageWebView.onPause()
            stageWebView.pauseTimers()
        }
    }

    override fun onResume() {
        val stageWebView = findViewById<WebView>(R.id.stage_webview)
        super.onResume()
        if (stageWebView != null) {
            stageWebView.onResume()
            stageWebView.resumeTimers()
        }
    }

    inner class MyWebClient : WebChromeClient() {

        private var mCustomView: View? = null
        private var mCustomViewCallback: WebChromeClient.CustomViewCallback? = null
        protected var mFullscreenContainer: FrameLayout? = null
        private var mOriginalOrientation: Int = 0
        private var mOriginalSystemUiVisibility: Int = 0

        override fun getDefaultVideoPoster(): Bitmap? {
            return if (this == null) {
                null
            } else BitmapFactory.decodeResource(resources, 2130837573)
        }

        override fun onHideCustomView() {
            (window.decorView as FrameLayout).removeView(this.mCustomView)
            this.mCustomView = null
            window.decorView.systemUiVisibility = this.mOriginalSystemUiVisibility
            requestedOrientation = this.mOriginalOrientation
            this.mCustomViewCallback!!.onCustomViewHidden()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            this.mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View, paramCustomViewCallback:
            WebChromeClient.CustomViewCallback
        ) {
            if (this.mCustomView != null) {
                onHideCustomView()
                return
            }
            this.mCustomView = paramView
            this.mOriginalSystemUiVisibility = window.decorView.systemUiVisibility
            this.mOriginalOrientation = requestedOrientation
            this.mCustomViewCallback = paramCustomViewCallback
            (window.decorView as FrameLayout).addView(
                this.mCustomView,
                FrameLayout.LayoutParams(-1, -1)
            )
            window.decorView.systemUiVisibility = 3846
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

    }
}