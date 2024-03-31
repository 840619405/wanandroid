package com.hjy.wanandroid.filter

import com.hjy.wanandroid.MyApplication

fun Int.getString(): String {
    return MyApplication.context.getString(this)
}

