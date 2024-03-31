package com.hjy.wanandroid.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.hjy.wanandroid.filter.toast
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.ActivityBase

class WebActivity : ActivityBase() {
    companion object {
        const val URL: String = "URL"
        const val TITEL: String = "TITLE"
    }

    private var url: String? = null
    private var title: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        getData()
        initActionBar()
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(title)) {
            "请打开正确的网址".toast(this)
        }
        val webView: WebView = findViewById<WebView>(R.id.web_view)
        val progressBar: ProgressBar = findViewById<ProgressBar>(R.id.progress_bar)
        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled =true
                blockNetworkImage = false
            }
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    Log.d("TAG", "onProgressChanged: "+newProgress)
                    progressBar.progress = newProgress
                }
            }
        }
        url?.let {
            webView.loadUrl(it)
        }
    }

    private fun getData() {
        url = intent.getStringExtra(URL)
        title = intent.getStringExtra(TITEL)
    }

    override fun initData() {

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_web
    }


    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title?.let {
            supportActionBar?.title = it
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}