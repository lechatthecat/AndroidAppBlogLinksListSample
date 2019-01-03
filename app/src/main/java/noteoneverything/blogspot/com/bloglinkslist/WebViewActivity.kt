package noteoneverything.blogspot.com.bloglinkslist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val intent = intent;
        val url = intent.getStringExtra("url");
        val browser = findViewById(R.id.browser) as WebView
        browser.getSettings().setJavaScriptEnabled(true)
        browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        browser.loadUrl(url)
    }
}
