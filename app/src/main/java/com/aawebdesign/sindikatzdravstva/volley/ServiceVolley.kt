package com.aawebdesign.sindikatzdravstva.volley

import com.aawebdesign.sindikatzdravstva.constants.Constants.Companion.URL
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class ServiceVolley : ServiceInterface {

    val TAG = ServiceVolley::class.java.simpleName

    override fun get(path: String, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjRequest = object : JsonObjectRequest(Method.GET, URL+path, null,
            Response.Listener<JSONObject> { response ->
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjRequest, TAG)
    }
}