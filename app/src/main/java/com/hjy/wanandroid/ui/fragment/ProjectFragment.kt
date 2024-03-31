package com.hjy.wanandroid.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hjy.wanandroid.filter.toast
import com.hjy.wanandroid.R
import com.hjy.wanandroid.base.FragmentBase
import com.hjy.wanandroid.mode.viewMode.ProjectViewMode
import com.hjy.wanandroid.ui.adpate.FragmentAdapter

class ProjectFragment : FragmentBase() {
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPage: ViewPager2
    private lateinit var mViewMode: ProjectViewMode
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var tabs: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project, container, false)
    }


    override fun initView(view: View) {
        mTabLayout = view.findViewById<TabLayout>(R.id.project_tab_layout)
        mViewPage = view.findViewById<ViewPager2>(R.id.project_view_pager)
    }

    override fun initData() {
        mViewMode = ViewModelProvider(this).get(ProjectViewMode::class.java)
        fragments = arrayListOf()
        tabs = arrayListOf()
        mViewMode.apply {
            _ProjectTypes.observe(this@ProjectFragment) {
                for (projectTypeBean in it) {
                    tabs.add(projectTypeBean.name)
                    fragments.add(ProjectListFragment.newInstance(projectTypeBean.id))
                }
                initTabLayoutAndViewPage()
            }
            _Error.observe(this@ProjectFragment) {
                context?.let { it1 -> it.toast(it1) }
            }
            if (tabs.isEmpty()) {
                getProjectTypes()
            }
        }
    }

    private fun initTabLayoutAndViewPage() {
        val mFragmentAdapter: FragmentAdapter? = childFragmentManager.let {
            FragmentAdapter(
                it,
                lifecycle,
                fragments,
                tabs
            )
        }
        mViewPage.adapter = mFragmentAdapter
        TabLayoutMediator(mTabLayout, mViewPage) { tab, position ->
            tab.text = mFragmentAdapter?.getItemName(position)
        }.attach()
    }
}