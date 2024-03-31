package com.hjy.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hjy.wanandroid.filter.toast
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.FragmentBase
import com.hjy.wanandroid.mode.bean.ArticleListBean
import com.hjy.wanandroid.mode.viewMode.ArticlesViewMode
import com.hjy.wanandroid.ui.activity.WebActivity
import com.hjy.wanandroid.ui.adpate.ArticleListAdapter


private const val CID = "param1"
private const val TYPE = "param2"

class ArticleListFragment : FragmentBase() {
    var TAG = "ArticleListFragment"
    private lateinit var mRecyclerView: RecyclerView
    private var isLoad: Boolean = true

    //private var view: View? = null
    var mView: View? = null

    //下拉刷新
    private lateinit var mSwipeRefLayout: SwipeRefreshLayout
    private lateinit var mArticles: ArrayList<ArticleListBean.Article>
    private lateinit var mArticleListAdapter: ArticleListAdapter
    private lateinit var mViewMode: ArticlesViewMode

    //文章类型
    private var type: Int = 0

    //文章TAB ID
    private var cid: Int = 0

    //搜索内容
    private var k: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(TYPE)
            cid = it.getInt(CID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mView != null) {
            val parent: ViewParent? = mView?.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(mView)
            }
            return mView
        }
        mView = inflater.inflate(R.layout.fragment_article_list, container, false)
        return mView
    }

    override fun initView(view: View) {
        mViewMode = ViewModelProvider(this).get<ArticlesViewMode>(ArticlesViewMode::class.java)
        mRecyclerView = view.findViewById(R.id.article_recycler_view)
        mSwipeRefLayout = view.findViewById(R.id.article_swipe_ref_layout)
        mSwipeRefLayout.setOnRefreshListener {
            Log.d(TAG, "setOnRefreshListener: ")
            mArticles?.let {
                it.clear()
            }
            //isLoad = true
            if (mViewMode != null) {
                mViewMode.getArticleList(true, cid, type, k)
            }
        }
        mArticles = arrayListOf();
        mArticleListAdapter =
            ArticleListAdapter(mArticles, object : ArticleListAdapter.ItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val article = mArticles.get(position)
                    val intent: Intent = Intent(activity, WebActivity::class.java).apply {
                        putExtra(WebActivity.URL, mArticles.get(position).link)
                        putExtra(WebActivity.TITEL, mArticles.get(position).title)
                    }
                    startActivity(intent)
                }
            })
        mArticleListAdapter.collectListener = object : ArticleListAdapter.OnCollectClickListener {
            override fun onClick(view: View, cid: Int, position: Int, collect: Boolean) {
                //收藏
                mViewMode.apply {
                    _Collect.observe(this@ArticleListFragment) {
                        if (it.success) {
                            mArticles[position].collect = !collect
                            mArticleListAdapter.notifyItemChanged(position)
                        } else {
                            it.err?.let {
                                context?.let { it1 -> it.toast(it1) }
                            }
                        }
                    }
                }.collectArticle(cid, collect)
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView.apply {
            adapter = mArticleListAdapter
            this.layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy < 0) {
                        return
                    }
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    // 设定一个距离底部的阈值，比如当剩下10个item时开始加载
                    val threshold = 1
                    if (totalItemCount <= (lastVisibleItem + threshold)) {
                        if (!isLoad && mViewMode != null) {
                            //isLoad = true
                            mViewMode.getArticleList(false, cid, type)
                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                }
            })
        }
    }

    override fun initData() {
        Log.d(TAG, "initData: ")
        mViewMode.apply {
            _UiModel.observe(this@ArticleListFragment) {
                Log.d(TAG, "_UiModel obs: " + it)
                mSwipeRefLayout.isRefreshing = it.loading
                if (it.error != null) {
                    context?.let { context ->
                        it.error.toast(context)
                    }
                } else {
                    isLoad = true
                }
                //加载更多
                if (it.end) {

                }
            }
            _Article.observe(this@ArticleListFragment) {
                Log.d(TAG, "_Article obs: ")
                mSwipeRefLayout.isRefreshing = false
                mArticles.addAll(it)
                mArticleListAdapter.isLastPage = it.isEmpty()
                if (it.isEmpty()) {
                    mRecyclerView.setHasFixedSize(true);
                    mArticleListAdapter.notifyItemChanged(mArticles.size)
                } else {
                    mArticleListAdapter.notifyDataSetChanged()
                }
                isLoad = false
            }
            if (mArticles.isEmpty() && type != 0) {
                getArticleList(true, cid, type)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    /**
     * 设置文章类型和搜索内容
     * @param isResident fragment是否常驻，主要用于判断生命周期
     */
    fun setTypeAndSearch(type: Int, k: String) {
        this.k = k
        this.type = type
        if (isResumed) {
            mArticles.clear()
            mArticleListAdapter.notifyDataSetChanged()
            mViewMode.getArticleList(true, 0, type, k)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(cid: Int, type: Int) =
            ArticleListFragment().apply {
                arguments = Bundle().apply {
                    putInt(CID, cid)
                    putInt(TYPE, type)
                }
            }
    }
}