package com.hjy.wanandroid.mode.bean

import java.io.Serializable

data class SystemBean(
    val articleList: List<Any>,
    val author: String,
    val children: List<Children>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
): Serializable {
    data class Children(
        val articleList: List<ArticleListBean.Article>,
        val author: String,
        val children: List<Any>,
        val courseId: Int,
        val cover: String,
        val desc: String,
        val id: Int,
        val lisense: String,
        val lisenseLink: String,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val type: Int,
        val userControlSetTop: Boolean,
        val visible: Int
    ):Serializable
}