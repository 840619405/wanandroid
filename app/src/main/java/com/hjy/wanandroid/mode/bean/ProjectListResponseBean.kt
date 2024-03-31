package com.hjy.wanandroid.mode.bean

data class ProjectListResponseBean(
    val curPage: Int,
    val datas: List<ProjectBean>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)