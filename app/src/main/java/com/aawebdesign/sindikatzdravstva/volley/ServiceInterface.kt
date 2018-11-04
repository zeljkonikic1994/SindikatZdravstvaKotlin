package com.aawebdesign.sindikatzdravstva.volley

import android.graphics.Bitmap
import org.json.JSONArray

interface ServiceInterface {
    fun getAllPosts(completionHandler: (response: JSONArray?) -> Unit)
    fun getImage(imageUrl: String?, completionHandler: (response: Bitmap?)-> Unit)
}