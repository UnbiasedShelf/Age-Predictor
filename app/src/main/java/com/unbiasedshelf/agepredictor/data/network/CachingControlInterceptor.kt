package com.unbiasedshelf.agepredictor.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class CachingControlInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        val maxAge = 60 * 60 * 24 // caching requests for a 1 day in seconds
        return originalResponse.newBuilder()
            .header("Cache-Control", "max-age=$maxAge")
            .build()
    }
}