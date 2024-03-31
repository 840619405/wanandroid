package com.hjy.wanandroid.mode.bean

data class WanBaseBean<T>(val errorCode: Int, val errorMsg: String, val data: T?) {
}