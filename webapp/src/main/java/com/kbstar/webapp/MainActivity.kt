package com.kbstar.webapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.kbstar.webapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settings = binding.webView.settings
        settings.javaScriptEnabled = true

        binding.webView.run {
            webViewClient = WebViewClient()
            //초기 html 로딩..
            loadUrl("http://10.10.220.79:7777/main.jbmovie")
        }
    }
}