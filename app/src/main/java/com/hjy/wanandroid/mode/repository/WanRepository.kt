package com.hjy.wanandroid.mode.repository

import com.hjy.wanandroid.mode.api.Result
import com.hjy.wanandroid.mode.api.WanClient
import com.hjy.wanandroid.mode.bean.*

class WanRepository : BaseRepository() {
    //获取首页文章
    suspend fun getArticleList(page: Int): Result<ArticleListBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getHomeArticles(page))
            }, "网络错误"
        )
    }

    //问答文章
    suspend fun getWenDanList(page: Int): Result<ArticleListBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getWenDanArticles(page))
            }, "网络错误"
        )
    }

    //广场文章
    suspend fun getPlazaArticles(page: Int): Result<ArticleListBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getPlazaArticles(page))
            }, "网络错误"
        )
    }

    //体系文章
    suspend fun getSystemTypeArticles(page: Int, cid: Int): Result<ArticleListBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getSystemArticles(page, cid))
            }, "网络错误"
        )
    }

    //体系分类
    suspend fun getSystemTypeArticles(): Result<List<SystemBean>> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getSystemArticles())
            }, "网络错误"
        )
    }


    //公众号文章
    suspend fun getWxArticleTab(): Result<List<SystemBean>> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getWxArticleTab())
            }, "网络错误"
        )
    }

    //搜索文章
    suspend fun getArticleQuery(page: Int, k: String): Result<ArticleListBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getArticleQuery(page, k))
            }, "网络错误"
        )
    }

    //常用网站列表
    suspend fun getFriends(): Result<List<FriendBean>> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getFriends())
            }, "网络错误"
        )
    }


    //搜索热词
    suspend fun getHotkey(): Result<List<FriendBean>> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getHotkey())
            }, "网络错误"
        )
    }


    suspend fun getProjectType(): Result<List<ProjectTypeBean>> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getProjectType())
            }, "网络错误"
        )
    }


    suspend fun getProjectList(page: Int, cid: Int): Result<ProjectListResponseBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getProjectList(page, cid))
            }, "网络错误"
        )
    }


    suspend fun getProjectList(): Result<UserInfoResponseBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getUserInfo())
            }, "网络错误"
        )
    }

    suspend fun login(username: String, password: String): Result<UserInfoBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.login(username, password))
            }, "网络错误"
        )
    }

    suspend fun register(username: String, password: String): Result<UserInfoBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.register(username, password,password))
            }, "网络错误"
        )
    }


    suspend fun loginOut(): Result<Any> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.loginOut())
            }, "网络错误"
        )
    }


    suspend fun collect(cid: Int): Result<Any> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.collectArticle(cid))
            }, "网络错误"
        )
    }


    suspend fun unCollect(cid: Int): Result<Any> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.unCollectArticle(cid))
            }, "网络错误"
        )
    }


    suspend fun getCollectList(page: Int): Result<ArticleListBean> {
        return apiCall(
            call = {
                executeResponse(WanClient.wanService.getCollectArticles(page))
            }, "网络错误"
        )
    }
}