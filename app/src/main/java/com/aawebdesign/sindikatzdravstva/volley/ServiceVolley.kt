package com.aawebdesign.sindikatzdravstva.volley

import android.util.Log
import com.aawebdesign.sindikatzdravstva.constants.Constants.Companion.URL
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class ServiceVolley : ServiceInterface {

    val TAG = ServiceVolley::class.java.simpleName

    override fun get(path: String, completionHandler: (response: JSONArray?) -> Unit) {
        val jsonObjRequest = object : JsonArrayRequest(Method.GET, URL + path, null,
            Response.Listener<JSONArray> { response ->
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                Log.e("RESPONSE ERROR {}", error.toString())
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
}