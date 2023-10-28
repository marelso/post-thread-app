package com.marelso.postthread.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marelso.postthread.data.Constants.PAGE_SIZE

class PostPagingSource(
    private val service: PostService
) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val currentPage = params.key ?: 0
            val response = service.get(page = currentPage, size = PAGE_SIZE)

            val items: List<Post> = response.body()?.content ?: listOf()

            LoadResult.Page(
                data = items,
                prevKey = if (currentPage <= 1) null else currentPage - 1,
                nextKey = if (items.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        TODO("Not yet implemented")
    }
}