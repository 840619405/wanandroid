package com.hjy.wanandroid.mode.api

sealed class Result<out T : Any> {
    data class Success<T : Any>(var data: T?) : Result<T>() {

    }

    data class Error(val exception: Exception, val errCode: Int = -1) : Result<Nothing>() {

    }
}