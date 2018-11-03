package com.aawebdesign.sindikatzdravstva.volley

import org.json.JSONArray

interface ServiceInterface {
    fun get(path: String, completionHandler: (response: JSONArray?) -> Unit)
}