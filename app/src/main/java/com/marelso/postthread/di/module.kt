package com.marelso.postthread.di

import com.marelso.postthread.data.Constants.SecondaryAPI
import com.marelso.postthread.data.Constants.PostAPI
import com.marelso.postthread.data.PostService
import com.marelso.postthread.data.SecondaryService
import com.marelso.postthread.ui.CreatePostViewModel
import com.marelso.postthread.ui.PostDetailViewModel
import com.marelso.postthread.ui.PostListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun provideRetrofitInstance(
    url: String
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

val appModule = module {
    factory(named(PostAPI.NAME)) {
        provideRetrofitInstance(PostAPI.URL)
    }
    factory {
        get<Retrofit>(named(PostAPI.NAME))
            .create(PostService::class.java)
    }

    factory(named(SecondaryAPI.NAME)) {
        provideRetrofitInstance(SecondaryAPI.URL)
    }
    factory {
        get<Retrofit>(named(SecondaryAPI.NAME))
            .create(SecondaryService::class.java)
    }

    viewModel { PostListViewModel(get(), get()) }
    viewModel { CreatePostViewModel(get()) }
    viewModel { (reference: Int) -> PostDetailViewModel(
        service = get(),
        reference = reference
    )}
}