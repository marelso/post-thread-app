package com.marelso.postthread.data

import com.marelso.postthread.data.Constants.PAGE_SIZE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {
    @GET("/posts")
    suspend fun get(
        @Query("page") page: Int,
        @Query("size") size: Int = PAGE_SIZE
    ): Response<Page<Post>>

    @GET("/posts/{id}")
    suspend fun get(@Path("id") id: Int): Response<Post>

    @POST
    suspend fun post(@Body post: Post): Response<Post>

    @PUT("/posts/{id}")
    suspend fun put(
        @Path("id") id: Int,
        @Body request: Post
    ): Response<Post>

    @PATCH("/posts/{id}")
    suspend fun patch(
        @Path("id") id: Int,
        @Query("status") status: Boolean
    ): Response<Unit>

    @DELETE("/posts/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}