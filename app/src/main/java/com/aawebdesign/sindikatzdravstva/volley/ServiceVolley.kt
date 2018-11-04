package com.aawebdesign.sindikatzdravstva.volley

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.aawebdesign.sindikatzdravstva.constants.Constants.Companion.URL
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class ServiceVolley : ServiceInterface {

    val TAG = ServiceVolley::class.java.simpleName

    override fun getAllPosts(completionHandler: (response: JSONArray?) -> Unit) {
        val jsonObjRequest = object : JsonArrayRequest(Method.GET, URL, null,
            Response.Listener<JSONArray> { response ->
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                Log.e("REQUESTS", "Error sending request: " + error.toString())
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjRequest, TAG)
    }

    override fun getImage(imageUrl: String?, completionHandler: (response: Bitmap?) -> Unit) {
        val imageRequest = object : ImageRequest(
            imageUrl,
            Response.Listener<Bitmap> { response ->
                completionHandler(response)
            },
            0,
            0,
            ImageView.ScaleType.CENTER_CROP,
            Bitmap.Config.RGB_565,
            Response.ErrorListener { error ->
                Log.e("REQUESTS", "Error sending request: " + error.toString())
                completionHandler(null)
            }){}
        BackendVolley.instance?.addToRequestQueue(imageRequest, TAG)
    }
}