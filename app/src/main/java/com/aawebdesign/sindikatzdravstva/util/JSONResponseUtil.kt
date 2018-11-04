package com.aawebdesign.sindikatzdravstva.util

import com.aawebdesign.sindikatzdravstva.dto.Post
import com.aawebdesign.sindikatzdravstva.util.HTMLUtil.Companion.clean
import com.aawebdesign.sindikatzdravstva.util.HTMLUtil.Companion.fixUnicodeDash
import org.json.JSONArray
import org.json.JSONObject

class JSONResponseUtil {
    companion object {
        fun parsePostArray(response: JSONArray): List<Post> {
            var posts = arrayListOf<Post>()
            for (i in 0 until response.length()) {
                var jsonPost: JSONObject = response.get(i) as JSONObject
                posts.add(parsePost(jsonPost))
            }
            return posts
        }
        private fun parsePost(jsonPost: JSONObject): Post {
            var title = jsonPost.get("title") as JSONObject
            var content = jsonPost.get("content") as JSONObject
            var image = jsonPost.get("featured_image_src") as String
            var lastModified = jsonPost.get("modified") as String
            return Post(
                jsonPost.get("id").toString(),
                fixUnicodeDash(title.get("rendered").toString()),
                clean(content.get("rendered").toString()),
                image,
                lastModified
            )
        }
    }

}