package com.hjy.wanandroid.mode.viewMode

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hjy.wanandroid.mode.bean.ArticleListBean
import com.hjy.wanandroid.mode.repository.WanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticlesViewMode : BaseViewMode() {
    companion object {
        const val TYPE_HOT: Int = 1 //热门
        const val TYPE_ANSWERS: Int = 2 //问答
        const val TYPE_PLAZA: Int = 3 //广场
        const val TYPE_SYSTEM: Int = 4 //体系
        const val TYPE_SEARCH: Int = 5 //搜索
        const val TYPE_BLOG: Int = 6 //公众号
        const val TYPE_COLLECT: Int = 7 //收藏列表
    }

    private val TAG: String = "ArticlesViewMode";
    var page: Int = -1//页码
    var articleRepository: WanRepository = WanRepository()

    //文章
    private val mArticle: MutableLiveData<List<ArticleListBean.Article>> =
        MutableLiveData<List<ArticleListBean.Article>>()
    val _Article: LiveData<List<ArticleListBean.Article>>
        get() = mArticle

    private val mCollect: MutableLiveData<CollectState> =
        MutableLiveData<CollectState>()
    val _Collect: LiveData<CollectState>
        get() = mCollect

    init {

    }

    fun getArticleList(refetch: Boolean, cid: Int, type: Int, k: String = "") {
        Log.d(TAG, "getArticleList: refetch=${refetch} cid=$cid type=$type}")
        mUiModel.value = UiModel(refetch, null, false)
        //判断type是否是指定页码
        if (refetch) page = -1
        viewModelScope.launch(Dispatchers.Main) {
            page++
            val result = withContext(context = Dispatchers.IO) {
                when (type) {
                    TYPE_HOT ->
                        articleRepository.getArticleList(page)
                    TYPE_ANSWERS ->
                        articleRepository.getWenDanList(page)
                    TYPE_PLAZA ->
                        articleRepository.getPlazaArticles(page)
                    TYPE_SYSTEM ->
                        articleRepository.getSystemTypeArticles(page, cid)
                    TYPE_BLOG ->
                        articleRepository.getSystemTypeArticles(page, cid)
                    TYPE_SEARCH ->
                        articleRepository.getArticleQuery(page, k)
                    TYPE_COLLECT ->
                        articleRepository.getCollectList(page)
                    else -> {
                        com.hjy.wanandroid.mode.api.Result.Error(Exception("文章类型错误"))
                    }
                }
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mArticle.value = result.data?.datas
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                mUiModel.value = UiModel(false, result.exception.message, false)
            } else {
                Log.d(TAG, "getArticleList error: ")
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }

    /**
     * 收藏文章
     * @param cid 文章ID
     * @param position 数据列表下标
     * @param collect 判断是否收藏，反向操作
     */
    fun collectArticle(cid: Int, collect: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                if (collect) {
                    articleRepository.unCollect(cid)
                } else {
                    articleRepository.collect(cid)
                }
            }
            if (result is com.hjy.wanandroid.mode.api.Result.Success) {
                mCollect.value = CollectState(true)
            } else if (result is com.hjy.wanandroid.mode.api.Result.Error) {
                mCollect.value = CollectState(false, result.exception.message)
            }
        }
    }


    data class CollectState(val success: Boolean, val err: String? = null) {

    }
}