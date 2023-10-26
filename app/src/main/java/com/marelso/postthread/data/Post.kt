package com.marelso.postthread.data

import java.util.Date

data class Post(
    val reference: Int,
    val headline: String,
    val description: String,
    val content: String,
    val previewImage: String,
    val bannerImage: String,
    val status: Boolean,
    val createdAt: Date
)