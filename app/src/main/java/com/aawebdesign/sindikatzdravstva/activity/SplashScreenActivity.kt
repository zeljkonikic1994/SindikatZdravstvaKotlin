package com.aawebdesign.sindikatzdravstva.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aawebdesign.sindikatzdravstva.R
import com.aawebdesign.sindikatzdravstva.db.PostDBHandler
import com.aawebdesign.sindikatzdravstva.dto.Post
import com.aawebdesign.sindikatzdravstva.tasks.UpdatePostsTask
import com.aawebdesign.sindikatzdravstva.volley.APIController
import com.aawebdesign.sindikatzdravstva.volley.JSONResponseUtil.Companion.parsePostArray
import com.aawebdesign.sindikatzdravstva.volley.ServiceVolley

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val postHandler = PostDBHandler(this, null, null, 1)

        val connected = hasNetworkConnection()

        if (connected) {
            val service = ServiceVolley()
            val apiController = APIController(service)

            apiController.getAllPosts { response ->
                if (response == null) {
                    startMainActivity(postHandler.getAll())
                    finish()
                } else {
                    val posts = parsePostArray(response)
                    UpdatePostsTask(this, posts, apiController).execute().get()
                    startMainActivity(posts)
                    finish()
                }
            }
        } else {
            Thread.sleep(2000)
            startMainActivity(postHandler.getAll())
            finish()
        }
    }

    private fun startMainActivity(postList: ArrayList<Post>) {
        val intent = MainActivity.newIntent(this, postList)
        startActivity(intent)
    }

    private fun hasNetworkConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else
            false
    }
}
