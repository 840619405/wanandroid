package com.hjy.wanandroid.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hjy.wanandroid.filter.toast
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.FragmentBase
import com.hjy.wanandroid.mode.bean.SystemBean
import com.hjy.wanandroid.mode.viewMode.SystemViewMode
import com.hjy.wanandroid.ui.adpate.SystemAdapter

class SystemFragment : FragmentBase() {
    private lateinit var mSystemRecyclerView: RecyclerView
    private lateinit var mSystemSwipeRefLayout: SwipeRefreshLayout
    private lateinit var mSystemBeans: ArrayList<SystemBean>
    private lateinit var systemAdapter: SystemAdapter
    private lateinit var mViewMode: SystemViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_system, container, false)
    }

    override fun initView(view: View) {
        mSystemRecyclerView = view.findViewById<RecyclerView>(R.id.system_recycler_view)
        mSystemSwipeRefLayout = view.findViewById(R.id.system_swipe_ref_layout)
        mSystemBeans = arrayListOf()
        systemAdapter = SystemAdapter(mSystemBeans, object : SystemAdapter.ItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable(ArticleTabFragment.TAB_LIST, mSystemBeans[position])
                bundle.putBoolean(ArticleTabFragment.IS_NV_SHOW, true)
                findNavController().navigate(R.id.action_homeFragment_self, bundle)
            }
        })
        mSystemRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = systemAdapter
        }
        mSystemSwipeRefLayout.setOnRefreshListener {
            mSystemBeans.clear()
            mViewMode.getSystemList()
        }
    }

    override fun initData() {
        mViewMode = ViewModelProvider(this).get<SystemViewMode>(SystemViewMode::class.java)
        mViewMode.apply {
            _System.observe(this@SystemFragment) {
                mSystemSwipeRefLayout.isRefreshing = false
                mSystemBeans.addAll(it)
                systemAdapter.notifyDataSetChanged()
            }
            _uiMode.observe(this@SystemFragment) {
                mSystemSwipeRefLayout.isRefreshing = it.loading
                context?.let { context ->
                    it.error?.toast(context)
                }
            }
            getSystemList()
        }
    }
}