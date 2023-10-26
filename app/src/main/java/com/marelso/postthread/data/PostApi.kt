package com.marelso.postthread.data

import com.marelso.postthread.data.Constants.POST_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostApi {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(POST_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: PostService by lazy {
        retrofit.create(PostService::class.java)
    }
}