package com.hjy.wanandroid.mode.repository

import com.hjy.wanandroid.mode.api.Result
import com.hjy.wanandroid.mode.bean.WanBaseBean
import java.lang.Exception

open class BaseRepository {
    //判断网络请求是否成功
    suspend fun <T : Any> apiCall(call: suspend () -> Result<T>, errMsg: String): Result<T> {
        return try {
            call()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    //判断业务是否成功
    suspend fun <T : Any> executeResponse(response: WanBaseBean<T>): Result<T> {
        if (response.errorCode == 0) {
            return Result.Success(response.data)
        } else {
            return Result.Error(Exception(response.errorMsg),response.errorCode)
        }
    }


}