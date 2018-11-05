package com.aawebdesign.sindikatzdravstva.tasks

import android.content.Context
import android.os.AsyncTask
import com.aawebdesign.sindikatzdravstva.db.ImageDBHandler
import com.aawebdesign.sindikatzdravstva.dto.Post
import com.aawebdesign.sindikatzdravstva.dto.PostImage
import com.aawebdesign.sindikatzdravstva.html.HTMLUtil
import com.aawebdesign.sindikatzdravstva.image.ImageUtil
import com.aawebdesign.sindikatzdravstva.volley.APIController

class DownloadImagesTask(
    val context: Context,
    val apiController: APIController,
    val imageHandler: ImageDBHandler,
    val postList: List<Post>
) : AsyncTask<Void, Void, Int>() {

    override fun doInBackground(vararg params: Void?): Int {
        for (post in postList) {
            val imageUrls = arrayListOf<String>()
            if (post.imgPath != null)
                imageUrls.add(post.imgPath)
            imageUrls.addAll(getImageUrls(post))

            for (imageUrl in imageUrls) {
                if (imageHandler.findImage(imageUrl) != null)
                    continue
                apiController.getImage(imageUrl) { response ->
                    if (response != null) {
                        var absolutePath =
                            ImageUtil.saveImageToInternalStorage(
                                context,
                                response,
                                imageUrl
                            )
                        imageHandler.insertImage(PostImage(imageUrl, absolutePath, post.id))
                    }
                }
            }
        }
        return 0
    }


    private fun getImageUrls(post: Post): List<String> {
        val imageUrls = arrayListOf<String>()
        val links = HTMLUtil.getLinks(post.content)
        for (link in links) {
            if (link.endsWith("jpg") || link.endsWith("jpeg") || link.endsWith("png"))
                imageUrls.add(link)
        }
        return imageUrls
    }


}