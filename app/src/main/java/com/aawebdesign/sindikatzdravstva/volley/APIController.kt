package com.aawebdesign.sindikatzdravstva.volley

import org.json.JSONArray

class APIController constructor(serviceInjection: ServiceInterface) : ServiceInterface {

    private val service: ServiceInterface = serviceInjection

    override fun get(path: String, completionHandler: (response: JSONArray?) -> Unit) {
        service.get(path, completionHandler)
    }
}