package com.marelso.postthread.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marelso.postthread.data.Post
import com.marelso.postthread.data.PostService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreatePostViewModel(private val service: PostService) : ViewModel() {
    var headline = mutableStateOf("")
    var description = mutableStateOf("")
    var content = mutableStateOf("")
    var previewImage = mutableStateOf("")
    var bannerImage = mutableStateOf("")
    var status = mutableStateOf(true)
    private val _postUiState = MutableStateFlow<CreateUiState>(
        CreateUiState.Loading
    )
    val state: StateFlow<CreateUiState> = _postUiState

    fun create() = viewModelScope.launch {
        _postUiState.value = CreateUiState.Loading

        val post = Post(
            headline = headline.value,
            description = description.value,
            content = content.value,
            previewImage = previewImage.value,
            bannerImage = bannerImage.value,
            status = status.value
        )

        val result = service.post(post)

        _postUiState.value = if (result.isSuccessful) CreateUiState.Success
        else CreateUiState.Error
    }
}

sealed class CreateUiState {
    object Loading : CreateUiState()
    object Error : CreateUiState()
    object Success : CreateUiState()
}