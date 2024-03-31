package com.hjy.wanandroid.mode.api

import com.hjy.wanandroid.util.PersistentCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseClient {
    val retrofit: Retrofit
        get() {
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .cookieJar(PersistentCookieJar())
                .build()
            return Retrofit.Builder()
                .baseUrl(WanService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
}