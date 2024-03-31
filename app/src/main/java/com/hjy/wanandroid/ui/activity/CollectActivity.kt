package com.hjy.wanandroid.ui.activity

import android.os.Bundle
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.ActivityBase
import com.hjy.wanandroid.mode.viewMode.ArticlesViewMode
import com.hjy.wanandroid.ui.fragment.ArticleListFragment

class CollectActivity : ActivityBase() {
    private lateinit var mArticleListFragment: ArticleListFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        mArticleListFragment =
            supportFragmentManager.findFragmentById(R.id.article_fragment) as ArticleListFragment
        mArticleListFragment.setTypeAndSearch(ArticlesViewMode.TYPE_COLLECT, "")
    }

    override fun initData() {
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_collect
    }
}