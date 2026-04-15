package com.example.learningportalapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var etUrl: EditText
    private lateinit var progressBar: ProgressBar

    private val homeUrl = "https://www.google.com"
    private val universityUrl =  "https://www.aiub.edu"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        etUrl = findViewById(R.id.etUrl)
        progressBar = findViewById(R.id.progressBar)

        val btnBack: Button = findViewById(R.id.btnBack)
        val btnForward: Button = findViewById(R.id.btnForward)
        val btnRefresh: Button = findViewById(R.id.btnRefresh)
        val btnHome: Button = findViewById(R.id.btnHome)
        val btnGo: Button = findViewById(R.id.btnGo)

        val btnGoogle: Button = findViewById(R.id.btnGoogle)
        val btnYoutube: Button = findViewById(R.id.btnYoutube)
        val btnWikipedia: Button = findViewById(R.id.btnWikipedia)
        val btnKhan: Button = findViewById(R.id.btnKhan)
        val btnUniversity: Button = findViewById(R.id.btnUniversity)

        setupWebView()

        btnBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                Toast.makeText(this, "No more history", Toast.LENGTH_SHORT).show()
            }
        }

        btnForward.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            } else {
                Toast.makeText(this, "No forward history", Toast.LENGTH_SHORT).show()
            }
        }

        btnRefresh.setOnClickListener {
            webView.reload()
        }

        btnHome.setOnClickListener {
            loadPage(homeUrl)
        }

        btnGo.setOnClickListener {
            loadFromAddressBar()
        }

        etUrl.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadFromAddressBar()
                true
            } else {
                false
            }
        }

        btnGoogle.setOnClickListener { loadPage("https://www.google.com") }
        btnYoutube.setOnClickListener { loadPage("https://www.youtube.com") }
        btnWikipedia.setOnClickListener { loadPage("https://www.wikipedia.org") }
        btnKhan.setOnClickListener { loadPage("https://www.khanacademy.org") }
        btnUniversity.setOnClickListener { loadPage(universityUrl) }

        if (isInternetAvailable()) {
            loadPage(homeUrl)
        } else {
            loadOfflinePage()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = ProgressBar.VISIBLE
                if (url != null) etUrl.setText(url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = ProgressBar.GONE
                if (url != null) etUrl.setText(url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                    loadOfflinePage()
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = ProgressBar.GONE
                } else {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
            }
        }
    }

    private fun loadFromAddressBar() {
        var url = etUrl.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Enter a URL", Toast.LENGTH_SHORT).show()
            return
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://$url"
        }

        loadPage(url)
    }

    private fun loadPage(url: String) {
        if (isInternetAvailable()) {
            webView.loadUrl(url)
        } else {
            loadOfflinePage()
        }
    }

    private fun loadOfflinePage() {
        webView.loadUrl("file:///android_asset/offline.html")
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}