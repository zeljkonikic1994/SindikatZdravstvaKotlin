package com.aawebdesign.sindikatzdravstva.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import com.aawebdesign.sindikatzdravstva.R
import com.aawebdesign.sindikatzdravstva.adapter.PostAdapter
import com.aawebdesign.sindikatzdravstva.constants.Constants.Companion.ALL_POSTS
import com.aawebdesign.sindikatzdravstva.constants.Messages.Companion.TIMEOUT_EXCEPTION_MESSAGE
import com.aawebdesign.sindikatzdravstva.db.PostDBHandler
import com.aawebdesign.sindikatzdravstva.dto.Post
import com.aawebdesign.sindikatzdravstva.util.JSONResponseUtil.Companion.parsePostArray
import com.aawebdesign.sindikatzdravstva.volley.APIController
import com.aawebdesign.sindikatzdravstva.volley.ServiceVolley
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val service: ServiceVolley? = ServiceVolley()
    private val apiController: APIController? = APIController(this!!.service!!)

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    fun startContactActivity(view: View) {
        val intent = ContactActivity.newIntent(this)
        startActivity(intent)
    }

    fun startPostActivity(post: Post){
        val intent = PostActivity.newIntent(this, post)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar: ProgressBar = this.progressBar
        setProgressBarVisibility(progressBar, View.VISIBLE)

        swipe_refresh_layout.setOnRefreshListener {
            loadAllPosts()
        }

        loadAllPosts()
        var listView = findViewById<ListView>(R.id.postList)
        listView.setOnItemClickListener{ _, _, position, _ ->
            startPostActivity(listView.adapter.getItem(position) as Post)
        }

        setProgressBarVisibility(progressBar, View.GONE)
    }

    private fun loadAllPosts() {
        var postList: List<Post>
        val postHandler = PostDBHandler(this, null, null, 1)
        val connected = hasNetworkConnection()
        Log.d("TAG", "CONNECTED: $connected")
        if (connected) {
            apiController?.get(ALL_POSTS) { response ->
                if (response == null) {
                    //get posts from db
                    //postList = loadPosts()
                    Toast.makeText(this, TIMEOUT_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show()
                } else {
                    postList = parsePostArray(response)
                    setPostAdapter(postList)
                    postHandler.addPosts(postList)
                }
            }
        } else {
            postList = postHandler.getAll()
            setPostAdapter(postList)
        }
        hideSwipeRefresh()
    }

    private fun setPostAdapter(postList: List<Post>) {
        var listView = findViewById<ListView>(R.id.postList)
        val postAdapter = PostAdapter(this, R.layout.post_list_view, postList)
        listView.adapter = postAdapter
        postAdapter.notifyDataSetChanged()
    }

    private fun hideSwipeRefresh() {
        if (swipe_refresh_layout.isRefreshing)
            swipe_refresh_layout.isRefreshing = false
    }

    private fun setProgressBarVisibility(progressBar: ProgressBar, visibility: Int) {
        this@MainActivity.runOnUiThread {
            progressBar.visibility = visibility
        }
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
