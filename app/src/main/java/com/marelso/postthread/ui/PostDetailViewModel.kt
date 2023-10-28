package com.marelso.postthread.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marelso.postthread.data.Post
import com.marelso.postthread.data.PostService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val service: PostService, private val reference: Int
) : ViewModel() {
    private var post: Post? = null
    private val _postUiState = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val postUiState: StateFlow<PostUiState> = _postUiState

    init {
        fetchContent()
    }

    private fun fetchContent() = viewModelScope.launch {
        _postUiState.value = PostUiState.Loading

        val result = service.get(reference)

        _postUiState.value = if (result.isSuccessful) result.body()?.let {
            PostUiState.Success(it)
        } ?: PostUiState.Error
        else PostUiState.Error
    }
}

sealed class PostUiState {
    object Loading : PostUiState()
    object Error : PostUiState()
    data class Success(val post: Post) : PostUiState()
}