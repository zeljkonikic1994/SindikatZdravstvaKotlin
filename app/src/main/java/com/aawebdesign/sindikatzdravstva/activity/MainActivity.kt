package com.aawebdesign.sindikatzdravstva.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import com.aawebdesign.sindikatzdravstva.R
import com.aawebdesign.sindikatzdravstva.adapter.PostAdapter
import com.aawebdesign.sindikatzdravstva.constants.Constants.Companion.POST_LIST
import com.aawebdesign.sindikatzdravstva.dto.Post
import com.aawebdesign.sindikatzdravstva.volley.APIController
import com.aawebdesign.sindikatzdravstva.volley.ServiceVolley
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context, postList: ArrayList<Post>): Intent {
            var intent = Intent(context, MainActivity::class.java)
            for((index, value) in postList.iterator().withIndex()){
                intent.putExtra(""+index, value)
            }
            return intent
        }
    }

    fun startContactActivity(view: View) {
        val intent = ContactActivity.newIntent(this)
        startActivity(intent)
    }

    fun startPostActivity(post: Post) {
        val intent = PostActivity.newIntent(this, post)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
        var postList = arrayListOf<Post>()
        for(i in 0..19){
            postList.add(i, intent.extras.get(""+i) as Post)
        }
//        Log.d("PARCELABLE", parcelableArrayExtra?.toString() ?: "null")
//        for (parcelable in parcelableArrayExtra) {
//            postList.add(parcelable as Post)
//        }
//
        setPostAdapter(postList)
//
//        val progressBar: ProgressBar = this.progressBar
//        setProgressBarVisibility(progressBar, View.VISIBLE)
//
////        swipe_refresh_layout.setOnRefreshListener {
////            loadAllPosts()
////        }
//
////        loadAllPosts()
//
        var listView = findViewById<ListView>(R.id.postList)
        listView.setOnItemClickListener { _, _, position, _ ->
            startPostActivity(listView.adapter.getItem(position) as Post)
        }
//
//        setProgressBarVisibility(progressBar, View.GONE)
    }

    private fun loadAllPosts() {
//        val postHandler = PostDBHandler(this, null, null, 1)
//        var postList = postHandler.getAll().reversed()
//        setPostAdapter(postList)
//        val connected = hasNetworkConnection()
//        if (connected) {
//            apiController?.getAllPosts { response ->
//                if (response == null) {
//                    postList = postHandler.getAll()
//                    Toast.makeText(this, TIMEOUT_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show()
//                } else {
//                    postList = parsePostArray(response)
//                    setPostAdapter(postList)
//                    postHandler.addPosts(postList)
//                    downloadImages(postList)
//                }
//            }
//        } else {
//            postList = postHandler.getAll()
//            setPostAdapter(postList)
//        }
//        hideSwipeRefresh()
    }

//    private fun downloadImages(postList: List<Post>) {
//        val task = DownloadImagesTask(postList, this)
//        task.execute()
//    }

    private fun hideSwipeRefresh() {
        if (swipe_refresh_layout.isRefreshing)
            swipe_refresh_layout.isRefreshing = false
    }

    private fun setPostAdapter(postList: List<Post>) {
        var listView = findViewById<ListView>(R.id.postList)
        val postAdapter = PostAdapter(this, R.layout.post_list_view, postList)
        listView.adapter = postAdapter
        postAdapter.notifyDataSetChanged()
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
