package com.aawebdesign.sindikatzdravstva.dto

import java.io.Serializable

data class Post(
    val id: String?,
    val title: String?,
    val content: String?,
    val imgPath: String?,
    val lastModified: String?
) : Serializable