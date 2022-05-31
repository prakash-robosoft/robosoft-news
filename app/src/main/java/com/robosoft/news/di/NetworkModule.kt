package com.robosoft.news.di

import com.robosoft.news.BuildConfig
import com.robosoft.news.data.remote.NewsApi
import com.robosoft.news.data.remote.util.RequestInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Network Koin dependencies
 */
val networkModule = module {
    factory { RequestInterceptor() }
    factory { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient): NewsApi {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.NEWS_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)
}

private fun provideOkHttpClient(interceptor: RequestInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
}
