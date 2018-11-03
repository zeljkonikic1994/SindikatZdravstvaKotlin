package com.aawebdesign.sindikatzdravstva.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.aawebdesign.sindikatzdravstva.R
import com.aawebdesign.sindikatzdravstva.dto.Post

class PostAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val posts: List<Post>) :
    ArrayAdapter<Post>(context, layoutResource, posts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view:View
        if (convertView == null)
            view = LayoutInflater.from(context).inflate(layoutResource, parent, false)
        else
            view = convertView

        val titleView = view.findViewById<TextView>(R.id.postTitle)
        val imageView = view.findViewById<ImageView>(R.id.postImage)

        titleView.text = getItem(position)?.title
        return view
    }

    override fun getItem(position: Int): Post? {
        return posts[position]
    }

}