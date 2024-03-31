package com.hjy.wanandroid.mode.api

object WanClient {
     val wanService:WanService by lazy {
        BaseClient.retrofit.create(WanService::class.java)
     }
}


