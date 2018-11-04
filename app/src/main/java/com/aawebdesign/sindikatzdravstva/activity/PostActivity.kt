package com.aawebdesign.sindikatzdravstva.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.aawebdesign.sindikatzdravstva.R
import com.aawebdesign.sindikatzdravstva.constants.Constants.Companion.POST
import com.aawebdesign.sindikatzdravstva.dto.Post

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

        var title = findViewById<TextView>(R.id.titlePost)
        var post = intent.extras.get(POST) as Post
        title.text = post.title
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            text.text = Html.fromHtml(post.content, Html.FROM_HTML_MODE_COMPACT)
//        }else{
//            text.text = Html.fromHtml(post.content)
//        }
    }
}
