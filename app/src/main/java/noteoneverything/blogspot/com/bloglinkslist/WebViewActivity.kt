package noteoneverything.blogspot.com.bloglinkslist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebViewClient



class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val intent = intent;
        val url = intent.getStringExtra("url");
        val browser = findViewById(R.id.browser) as WebView
        browser.getSettings().setJavaScriptEnabled(true)
        browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        browser.webViewClient = SSLTolerentWebViewClient()
        browser.loadUrl(url)
    }

    private inner class SSLTolerentWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed() // Ignore SSL certificate errors
        }

    }
}
