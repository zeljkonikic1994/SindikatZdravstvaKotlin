package com.aawebdesign.sindikatzdravstva.tasks

import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import com.aawebdesign.sindikatzdravstva.db.ImageDBHandler
import com.aawebdesign.sindikatzdravstva.db.PostDBHandler
import com.aawebdesign.sindikatzdravstva.dto.Post
import com.aawebdesign.sindikatzdravstva.dto.PostImage
import com.aawebdesign.sindikatzdravstva.html.HTMLUtil
import com.aawebdesign.sindikatzdravstva.image.ImageUtil
import com.aawebdesign.sindikatzdravstva.volley.APIController
import java.nio.file.Files
import java.nio.file.Paths

class UpdatePostsTask(val context: Context, val postList: List<Post>, val apiController: APIController) : AsyncTask<Void, Void, Int>() {

    private val postsHandler = PostDBHandler(context, null, null, 1)
    private val imageHandler = ImageDBHandler(context, null, null, 1)

    override fun doInBackground(vararg params: Void?): Int {
        var postsFromDB = postsHandler.getAll()
        for (post in postList) {
            if (getPost(postsFromDB, post.id) == null) {
                postsHandler.deletePost(post.id)
                Log.d("UpdatePostsTask", "Deleting post: " + post.id)

                val postImages = imageHandler.findImagesForPost(post.id)
                for (postImage in postImages) {
                    imageHandler.deleteImage(postImage.url)
                    deleteDownloadedFile(postImage.internalLocation)
                }
            }
        }
        postsHandler.addPosts(postList)
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

    private fun getPost(postList: List<Post>, id: String?): Post? {
        for (post in postList) {
            if (post.id.equals(id))
                return post
        }
        return null
    }

    private fun deleteDownloadedFile(internalLocation: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.deleteIfExists(Paths.get(internalLocation))
        }
    }

}
