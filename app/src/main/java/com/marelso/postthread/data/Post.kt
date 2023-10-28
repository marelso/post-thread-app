package com.marelso.postthread.data

import java.util.Date

data class Post(
    var reference: Int? = null,
    val headline: String,
    val description: String,
    val content: String,
    val previewImage: String,
    val bannerImage: String,
    val status: Boolean,
    var createdAt: Date? = null
)