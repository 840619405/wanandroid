package com.hjy.wanandroid.mode.api

import com.hjy.wanandroid.mode.bean.*
import retrofit2.http.*

interface WanService {
    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    //首页文章
    @GET("article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): WanBaseBean<ArticleListBean>

    //收藏列表文章
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectArticles(@Path("page") page: Int): WanBaseBean<ArticleListBean>


    //问答文章
    @GET("wenda/list/{page}/json")
    suspend fun getWenDanArticles(@Path("page") page: Int): WanBaseBean<ArticleListBean>

    //广场文章
    @GET("user_article/list/{page}/json")
    suspend fun getPlazaArticles(@Path("page") page: Int): WanBaseBean<ArticleListBean>

    //体系文章
    @GET("tree/json")
    suspend fun getSystemArticles(): WanBaseBean<List<SystemBean>>

    //指定类型文章
    @GET("article/list/{page}/json")
    suspend fun getSystemArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): WanBaseBean<ArticleListBean>

    //获取公众号tab
    @GET("wxarticle/chapters/json")
    suspend fun getWxArticleTab(): WanBaseBean<List<SystemBean>>


    //获取公众号tab
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun getArticleQuery(
        @Path("page") page: Int,
        @Field("k") k: String
    ): WanBaseBean<ArticleListBean>

    //常用网站
    @GET("friend/json")
    suspend fun getFriends(): WanBaseBean<List<FriendBean>>

    //搜索热词
    @GET("hotkey/json")
    suspend fun getHotkey(): WanBaseBean<List<FriendBean>>

    @GET("project/tree/json")
    suspend fun getProjectType(): WanBaseBean<List<ProjectTypeBean>>

    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): WanBaseBean<ProjectListResponseBean>

    @GET("user/lg/userinfo/json")
    suspend fun getUserInfo(): WanBaseBean<UserInfoResponseBean>

    //获取公众号tab
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String, @Field("password") password: String
    ): WanBaseBean<UserInfoBean>


    @POST("user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password") repassword: String = password
    ): WanBaseBean<UserInfoBean>

    @GET("user/logout/json")
    suspend fun loginOut(): WanBaseBean<Any>

    @POST("lg/collect/{cid}/json")
    suspend fun collectArticle(@Path("cid") cid: Int): WanBaseBean<Any>

    @POST("lg/uncollect_originId/{cid}/json")
    suspend fun unCollectArticle(@Path("cid") cid: Int): WanBaseBean<Any>

}

