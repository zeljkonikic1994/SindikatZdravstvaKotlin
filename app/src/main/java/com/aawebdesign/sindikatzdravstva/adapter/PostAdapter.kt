package com.aawebdesign.sindikatzdravstva.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.support.annotation.LayoutRes
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.aawebdesign.sindikatzdravstva.R
import com.aawebdesign.sindikatzdravstva.dto.Post
import com.aawebdesign.sindikatzdravstva.image.ImageUtil.Companion.getImagePath
import java.io.File
import java.io.FileInputStream

class PostAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val posts: List<Post>) :
    ArrayAdapter<Post>(context, layoutResource, posts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View
        if (convertView == null)
            view = LayoutInflater.from(context).inflate(layoutResource, parent, false)
        else
            view = convertView

        var post = getItem(position)

        val titleView = view.findViewById<TextView>(R.id.postTitle)
        titleView.text = post?.title

        val imageView = view.findViewById<ImageView>(R.id.postImage)

        val file = File(getImagePath(post?.imgPath))
        if (file == null)
            imageView.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.post_list_default,
                    null
                )
            )
        else
            imageView.setImageBitmap(BitmapFactory.decodeStream(FileInputStream(file)))
        return view
    }

    override fun getItem(position: Int): Post? {
        return posts[position]
    }

}