package com.aawebdesign.sindikatzdravstva.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.widget.TextView
import com.aawebdesign.sindikatzdravstva.R
import com.aawebdesign.sindikatzdravstva.constants.Constants.Companion.POST
import com.aawebdesign.sindikatzdravstva.db.ImageDBHandler
import com.aawebdesign.sindikatzdravstva.dto.Post
import com.aawebdesign.sindikatzdravstva.image.ImageGetter

class PostActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context, post: Post): Intent {
            var intent = Intent(context, PostActivity::class.java)
            intent.putExtra(POST, post)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        var post = intent.extras.get(POST) as Post

        var title = findViewById<TextView>(R.id.titlePost)
        title.text = post.title

        var content = findViewById<TextView>(R.id.contentPost)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content.text = Html.fromHtml(post.content, Html.FROM_HTML_MODE_COMPACT, ImageGetter(this), null)
        } else {
            content.text = Html.fromHtml(post.content)
        }
    }

}
