package com.hjy.wanandroid.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hjy.wanandroid.filter.getString
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.FragmentBase
import com.hjy.wanandroid.mode.bean.SystemBean
import com.hjy.wanandroid.mode.viewMode.ArticlesViewMode
import com.hjy.wanandroid.mode.viewMode.WxArticleTabViewMode
import com.hjy.wanandroid.ui.adpate.FragmentAdapter


class ArticleTabFragment : FragmentBase() {
    // TODO: Rename and change types of parameters
    private var tabType = 0
    private var mSystemBean: SystemBean? = null
    private var isNVShow: Boolean = false
    val fragments: MutableList<Fragment> = mutableListOf()
    var tabItems: ArrayList<String> = arrayListOf()
    lateinit var bar: LinearLayout
    lateinit var title: TextView
    lateinit var mTablayout: TabLayout
    private var mFragmentAdapter: FragmentAdapter? = null

    private val mViewMode: WxArticleTabViewMode by lazy {
        ViewModelProvider(this).get<WxArticleTabViewMode>(WxArticleTabViewMode::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mSystemBean = it.getSerializable(TAB_LIST) as SystemBean?
            isNVShow = it.getBoolean(IS_NV_SHOW)
            tabType = it.getInt(TAB_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initData() {

    }

    override fun initView(view: View) {
        val viewPager2 = view.findViewById<ViewPager2>(R.id.view_pager)
        mTablayout = view.findViewById<TabLayout>(R.id.tab_layout)
        initNavBar(view)
        getTabs()
        mFragmentAdapter = activity?.supportFragmentManager?.let {
            FragmentAdapter(
                it,
                lifecycle,
                fragments,
                tabItems
            )
        }
        viewPager2.adapter = mFragmentAdapter
        TabLayoutMediator(mTablayout, viewPager2) { tab, position ->
            tab.text = mFragmentAdapter?.getItemName(position)
        }.attach()
        initTabLayout(mTablayout)
    }

    //初始化导航栏
    private fun initNavBar(view: View) {
        bar = view.findViewById(R.id.home_bar_linlayout)
        title = view.findViewById(R.id.home_title)
        if (isNVShow) {
            bar.visibility = View.VISIBLE
        } else {
            bar.visibility = View.GONE
        }
        val back: ImageView = view.findViewById(R.id.home_back_iv)
        back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    //设置tabLayout，主要是测量tabs宽度，决定tabLayout滑动类型
    private fun initTabLayout(tabLayout: TabLayout) {
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        val parentView = tabLayout.parent as? ViewGroup
        tabLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 防止多次调用，布局稳定后移除监听器
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tabLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    tabLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                // 获取TabLayout及其父布局的宽度
                val parentWidth = parentView?.width ?: 0
                var totalTabWidth = 0
                // 计算所有Tab所需的总宽度
                for (i in 0 until tabLayout.tabCount) {
                    val tab = tabLayout.getTabAt(i)
                    val tabView = tab?.customView ?: tab?.view
                    if (tabView != null && tabView.width > 0) {
                        totalTabWidth += tabView.width
                    }
                }
                // 根据总宽度判断是否超过屏幕宽度
                if (totalTabWidth > parentWidth) {
                    // 设置TabLayout为滚动模式
                    tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
                } else {
                    // 若宽度不足，可以保持默认模式或设置为固定模式
                    tabLayout.tabMode = TabLayout.MODE_FIXED
                }
            }
        })
    }

    //获取tab的item和fragmentList
    private fun getTabs() {
        tabItems.clear()
        fragments.clear()
        when (tabType) {
            PARAM_TAB_TYPE_BLOG -> {
                getWxArticleTab()
            }
            else -> {
                if (mSystemBean != null) {
                    mSystemBean?.let {
                        title.text = mSystemBean!!.name
                        for (child in mSystemBean!!.children) {
                            tabItems.add(child.name)
                            fragments.add(
                                ArticleListFragment.newInstance(
                                    child.id,
                                    ArticlesViewMode.TYPE_SYSTEM
                                )
                            )
                        }
                    }
                } else {
                    val homeArticles: Array<String> =
                        resources.getStringArray(R.array.home_articles)
                    for (homeArticle in homeArticles) {
                        tabItems.add(homeArticle)
                    }
                    val intArray: IntArray = resources.getIntArray(R.array.home_article_types)
                    for (i in 0 until homeArticles.size) {
                        if (homeArticles[i] != R.string.system.getString()) {
                            fragments.add(ArticleListFragment.newInstance(0, intArray[i]))
                        } else {
                            fragments.add(SystemFragment())
                        }
                    }
                }
            }
        }
    }

    //获取微信公众号tab
    private fun getWxArticleTab() {
        mViewMode._WxArticleTab.apply {
            observe(this@ArticleTabFragment) {
                for (systemBean in it) {
                    tabItems.add(systemBean.name)
                    fragments.add(
                        ArticleListFragment.newInstance(
                            systemBean.id,
                            ArticlesViewMode.TYPE_BLOG
                        )
                    )
                }
                mFragmentAdapter?.let {
                    it.notifyDataSetChanged()
                    initTabLayout(mTablayout)
                }
            }
        }
        mViewMode.getWxArticleTab()
    }


    override fun onPause() {
        super.onPause()
        // isShowBar(false)
    }

    companion object {
        const val IS_NV_SHOW: String = "param1"
        const val TAB_LIST = "param2"
        const val TAB_TYPE = "param3"

        //公众号
        const val PARAM_TAB_TYPE_BLOG = 1
    }
}