package com.hjy.wanandroid.mode.bean

import android.text.TextUtils

/**
 * 常用网站和热词
 * type 1表示热词，2表示常用网站
 */
data class FriendBean(
    val category: String,
    val icon: String,
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int,
    val type: Int = if (TextUtils.isEmpty(link)) 1 else 2 //有网站的表示常用网站，否则为热词
)