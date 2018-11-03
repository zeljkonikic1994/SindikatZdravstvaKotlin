package com.aawebdesign.sindikatzdravstva.volley

import org.json.JSONObject

interface ServiceInterface {
    fun get(path: String, completionHandler: (response: JSONObject?) -> Unit)
}