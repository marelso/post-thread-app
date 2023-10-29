package com.marelso.postthread.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marelso.postthread.data.Post
import com.marelso.postthread.data.PostService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val service: PostService, var reference: Int
) : ViewModel() {
    private val _postUiState = MutableStateFlow<PostUiState>(
        PostUiState.Loading
    )
    val postUiState: StateFlow<PostUiState> = _postUiState

    fun fetchContent() = viewModelScope.launch {
        val result = service.get(reference)

        _postUiState.value = if (result.isSuccessful) result.body()?.let {
            PostUiState.Success(it)
        } ?: PostUiState.Error(subtitle = "Resource not found")
        else PostUiState.Error(
            subtitle = "There was an error fetching: ${result.code()}"
        )
    }

    fun refresh() = viewModelScope.launch {
        _postUiState.value = PostUiState.Loading
        fetchContent()
    }

    fun changePostStatus(status: Boolean) = viewModelScope.launch {
        service.patch(reference, status)
    }

    fun deletePost() = viewModelScope.launch {
        service.delete(reference)
    }
}

sealed class PostUiState {
    object Loading : PostUiState()
    data class Error(
        val headline: String = "Something went wrong",
        val subtitle: String
    ) : PostUiState()

    data class Success(val post: Post) : PostUiState()
}