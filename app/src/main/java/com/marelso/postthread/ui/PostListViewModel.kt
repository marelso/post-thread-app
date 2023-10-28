package com.marelso.postthread.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.marelso.postthread.data.PostApi
import com.marelso.postthread.data.PostPagingSource
import com.marelso.postthread.data.PostService
import com.marelso.postthread.data.SecondaryService

class PostListViewModel(
    private val service: PostService,
    private val secondaryService: SecondaryService
) : ViewModel() {
    private val postApiService = PostApi.instance
    val pagingData = Pager(PagingConfig(pageSize = 20)) {
        PostPagingSource(service)
    }.flow.cachedIn(viewModelScope)
}