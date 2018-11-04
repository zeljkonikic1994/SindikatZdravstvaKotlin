package com.aawebdesign.sindikatzdravstva.volley

import android.graphics.Bitmap
import org.json.JSONArray

class APIController constructor(serviceInjection: ServiceInterface) : ServiceInterface {

    private val service: ServiceInterface = serviceInjection

    override fun getAllPosts(completionHandler: (response: JSONArray?) -> Unit) {
        service.getAllPosts(completionHandler)
    }

    override fun getImage(imageUrl: String?, completionHandler: (response: Bitmap?) -> Unit) {
        service.getImage(imageUrl, completionHandler)
    }
}