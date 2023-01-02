package com.unbiasedshelf.agepredictor.data.di

import android.content.Context
import android.content.SharedPreferences
import com.unbiasedshelf.agepredictor.data.network.AgifyService
import com.unbiasedshelf.agepredictor.data.repository.AgifyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String = "https://api.agify.io/"

    @Provides
    fun provideRetrofit(
        @Named("BASE_URL") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideAgifyService(
        retrofit: Retrofit
    ): AgifyService = retrofit.create(AgifyService::class.java)

    @Provides
    fun provideFavoriteSharedPreferences(
        @ApplicationContext application: Context
    ): SharedPreferences = application.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    @Provides
    fun provideAgifyRepository(
        favoriteSharedPreferences: SharedPreferences,
        agifyService: AgifyService
    ): AgifyRepository = AgifyRepository(favoriteSharedPreferences, agifyService)
}