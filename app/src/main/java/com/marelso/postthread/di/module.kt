package com.marelso.postthread.di

import com.marelso.postthread.data.Constants.PostAPI
import com.marelso.postthread.data.PostService
import com.marelso.postthread.ui.ListViewModel
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
    factory { get<Retrofit>(named(PostAPI.NAME)).create(PostService::class.java) }
    viewModel { ListViewModel(get()) }
}