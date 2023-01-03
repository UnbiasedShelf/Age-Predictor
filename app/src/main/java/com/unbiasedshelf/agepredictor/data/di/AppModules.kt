package com.unbiasedshelf.agepredictor.data.di

import android.content.Context
import android.content.SharedPreferences
import com.unbiasedshelf.agepredictor.data.network.AgifyService
import com.unbiasedshelf.agepredictor.data.network.CachingControlInterceptor
import com.unbiasedshelf.agepredictor.data.repository.AgifyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    @Provides
    @Singleton
    @Named("BASE_URL")
    fun provideBaseUrl(): String = "https://api.agify.io/"

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("BASE_URL") baseUrl: String,
        @ApplicationContext application: Context
    ): Retrofit {
        val sizeOfCache = (1024 * 1024).toLong() // 1 MiB
        val cache = Cache(File(application.cacheDir, "agify_cache"), sizeOfCache)
        val client = OkHttpClient()
            .newBuilder()
            .cache(cache)
            .addNetworkInterceptor(CachingControlInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAgifyService(retrofit: Retrofit): AgifyService = retrofit.create(AgifyService::class.java)

    @Provides
    @Singleton
    fun provideFavoriteSharedPreferences(@ApplicationContext application: Context): SharedPreferences =
        application.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideAgifyRepository(
        favoriteSharedPreferences: SharedPreferences,
        agifyService: AgifyService
    ): AgifyRepository = AgifyRepository(favoriteSharedPreferences, agifyService)
}