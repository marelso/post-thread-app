package com.marelso.postthread.ui

import androidx.lifecycle.ViewModel
import com.marelso.postthread.data.PostApi

class ListViewModel(): ViewModel() {
    private val postApiService = PostApi.instance
}