package noteoneverything.blogspot.com.bloglinkslist

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.ViewGroup
import android.util.DisplayMetrics
import android.widget.LinearLayout
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_NEUTRAL
import android.content.Intent
import android.support.v7.app.AlertDialog


class MainActivity : AppCompatActivity(), View.OnClickListener {

    var loadMoreLinkLayoutId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.load(5, this)
    }

    override fun onClick(v : View){
        this.load(5, this)
    }

    // Function to load more links
    private fun load(numOfLoad : Int, context : Context){
        // If the load-more-links always exists, remove it.
        if(findViewById(this.loadMoreLinkLayoutId) as? LinearLayout != null) {
            val oldLoadMoreLinkLayout = findViewById(this.loadMoreLinkLayoutId) as LinearLayout
            (oldLoadMoreLinkLayout.getParent() as ViewGroup).removeView(oldLoadMoreLinkLayout)
        }
        // Create links as many as designated
        for (i in 1..numOfLoad) {
            this.createLinkView(
                this,
                "https://2.bp.blogspot.com/-wqB2qH-DUNA/XC3KJ0-ZW6I/AAAAAAAAHb4/frYEljJQ2z4AjXZz772isHM_KNIkSyEDgCLcBGAs/s320/no_image.jpg",
                "https://noteoneverything.blogspot.com/",
                "Notes on everything",
                "test test test test test test test test test test test test test test test test test test test test test test test test test test test test",
                "2018/1/1 20:00:00"
            )
        }
        // Create the load-more-links
        createMoreLinkTextView(context)
    }

    private fun createMoreLinkTextView(context : Context){
        val loadMoreLinkLayout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(10,10,10,10)
        loadMoreLinkLayout.isClickable = true
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        loadMoreLinkLayout.setBackgroundResource(outValue.resourceId)
        loadMoreLinkLayout.layoutParams = layoutParams
        loadMoreLinkLayout.orientation = LinearLayout.HORIZONTAL
        loadMoreLinkLayout.gravity = Gravity.BOTTOM

        val text = TextView(context)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        text.layoutParams = params
        text.gravity = Gravity.CENTER
        text.setText("Load more")
        loadMoreLinkLayout.addView(text)
        loadMoreLinkLayout.setOnClickListener(this)
        this.loadMoreLinkLayoutId = View.generateViewId()
        loadMoreLinkLayout.id = this.loadMoreLinkLayoutId

        val linearlayout = findViewById(R.id.linearlayout) as LinearLayout
        linearlayout.addView(loadMoreLinkLayout)
    }

    // Create the horizontal layout and its content
    private fun createLinkView(context: Context, imageUrl: String,
                               webPageUrl: String, title : String, description : String, timestamp : String){
        // 画面幅取得
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val dispWidth = displayMetrics.widthPixels

        // Create the horizontal layout
        val parent = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins((dispWidth.toFloat() * 0.005).toInt(),15,(dispWidth.toFloat() * 0.005).toInt(),15)
        parent.isClickable = true
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        parent.setBackgroundResource(outValue.resourceId)
        parent.layoutParams = layoutParams
        parent.orientation = LinearLayout.HORIZONTAL
        parent.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            // To pass any data to next activity
            intent.putExtra("url", webPageUrl)
            // start next activity
            startActivity(intent)
        }

        // Create image from image url
        val iv = ImageView(context)
        try {
            DownloadImageTask(iv).execute(imageUrl);
        } catch (e: Exception) {
            iv.setImageResource(R.drawable.ic_launcher_foreground)
            System.out.println(e)
        }

        val imageWidth = (dispWidth.toFloat() * 0.25).toInt()
        val imageHeight = imageWidth
        val params = ViewGroup.LayoutParams(imageWidth, imageHeight)
        iv.layoutParams = params
        iv.adjustViewBounds = true
        iv.scaleType = ImageView.ScaleType.CENTER_CROP

        // Set the image in horizontal layout
        parent.addView(iv)
        parent.tag = "imageParentLayout"

        // Create the title view
        val textParent = LinearLayout(context)
        val textWidth = (dispWidth.toFloat() * 0.70).toInt()
        val textLayoutParams = LinearLayout.LayoutParams(textWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        textLayoutParams.leftMargin = (dispWidth.toFloat() * 0.006).toInt()
        textParent.layoutParams = textLayoutParams
        textParent.orientation = LinearLayout.VERTICAL

        val text = TextView(context)
        val textParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        text.layoutParams = textParams
        text.gravity = Gravity.CENTER_VERTICAL
        // TO DO: Too long title must be adjusted
        text.setText(description)
        textParent.addView(text)

        // ブログタイトルViewを作成・セット
        val titleTextParent = LinearLayout(context)
        val titleTextParentLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
        titleTextParent.layoutParams = titleTextParentLayoutParams
        titleTextParent.orientation = LinearLayout.HORIZONTAL
        titleTextParent.gravity = Gravity.BOTTOM

        val titleText = TextView(context)
        val titleTextParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f)
        titleText.layoutParams = titleTextParams
        titleText.gravity = Gravity.BOTTOM
        // TO DO: タイトルが長すぎる場合に調整する
        titleText.setText(title)
        titleText.textSize = 9.toFloat()
        titleTextParent.addView(titleText)

        // タイムスタンプを作成・セット
        val timestampText = TextView(context)
        val timestampTextParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f)
        timestampText.layoutParams = timestampTextParams
        timestampText.gravity = Gravity.RIGHT
        timestampText.textSize = 8.toFloat()
        timestampText.setText(timestamp)
        titleTextParent.addView(timestampText)

        textParent.addView(titleTextParent)

        parent.addView(textParent)
        // 作成したレイアウトを画面にセット
        val linearlayout = findViewById(R.id.linearlayout) as LinearLayout
        linearlayout.addView(parent)

    }

    // 非同期に画像をURLから読み込む
    private inner class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Error", e.message)
                e.printStackTrace()
            }

            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }
}
